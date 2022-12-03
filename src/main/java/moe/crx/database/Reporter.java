package moe.crx.database;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.jooq.tables.records.OrganizationsRecord;
import moe.crx.jooq.tables.records.ProductsRecord;
import moe.crx.reports.ProductSummary;
import moe.crx.reports.ProductsReport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static moe.crx.jooq.Tables.*;

public final class Reporter extends HikariConnectable {

    @Inject
    public Reporter(@NotNull HikariDataSource dataSource) {
        super(dataSource);
    }

    private static final Table<Record> FILTERED = DSL.table("filtered");
    private static final Field<Object> FILTERED_INN = DSL.field("filtered.inn");
    private static final Field<Object> FILTERED_AMOUNT = DSL.field("filtered.amount");
    private static final Field<BigDecimal> SUM_AMOUNT = DSL.sum(POSITIONS.AMOUNT).as("amount");

    public @NotNull List<@NotNull OrganizationsRecord> getTopSuppliers() {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            var filtered =
                    ctx.select(ORGANIZATIONS.INN, ORGANIZATIONS.NAME, ORGANIZATIONS.GIRO, SUM_AMOUNT)
                            .from(ORGANIZATIONS)
                            .join(RECEIPTS).on(RECEIPTS.ORGANIZATION_ID.eq(ORGANIZATIONS.INN))
                            .join(POSITIONS).on(RECEIPTS.ID.eq(POSITIONS.RECEIPT_ID))
                            .groupBy(ORGANIZATIONS.INN)
                            .orderBy(DSL.sum(POSITIONS.AMOUNT).desc())
                            .limit(10)
                            .asTable(FILTERED);
            return ctx.select(ORGANIZATIONS.INN, ORGANIZATIONS.NAME, ORGANIZATIONS.GIRO)
                    .from(ORGANIZATIONS)
                    .leftOuterJoin(filtered).on(FILTERED_INN.eq(ORGANIZATIONS.INN))
                    .orderBy(FILTERED_AMOUNT.desc().nullsLast())
                    .limit(10)
                    .fetch()
                    .map(record -> record.into(ORGANIZATIONS));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public @NotNull List<@NotNull OrganizationsRecord> getSuppliersByProductAndLimit(@NotNull Map<@NotNull ProductsRecord, @NotNull Integer> limits) {
        if (limits.size() == 0)
            return List.of();
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            Condition where = DSL.noCondition();
            for (Map.Entry<ProductsRecord, Integer> entry : limits.entrySet()) {
                where = where.or(POSITIONS.PRODUCT_ID.eq(entry.getKey().getCode()).and(POSITIONS.AMOUNT.ge(entry.getValue())));
            }
            return ctx.select(ORGANIZATIONS.INN, ORGANIZATIONS.NAME, ORGANIZATIONS.GIRO).from(ORGANIZATIONS)
                    .join(RECEIPTS).on(RECEIPTS.ORGANIZATION_ID.eq(ORGANIZATIONS.INN))
                    .join(POSITIONS).on(POSITIONS.RECEIPT_ID.eq(RECEIPTS.ID))
                    .where(where)
                    .groupBy(ORGANIZATIONS.INN)
                    .having(DSL.count().ge(limits.size()))
                    .fetch()
                    .map(record -> record.into(ORGANIZATIONS));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    private static final Field<BigDecimal> AVERAGE_PRICE = DSL.avg(POSITIONS.PRICE).as("avg");

    public @NotNull Map<@NotNull ProductsRecord, @NotNull Double> getAveragePriceInPeriod(@NotNull LocalDate begin, @Nullable LocalDate end) {
        if (end == null)
            end = begin;
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return ctx.select(PRODUCTS.CODE, PRODUCTS.NAME, AVERAGE_PRICE).from(PRODUCTS)
                    .join(POSITIONS).on(POSITIONS.PRODUCT_ID.eq(PRODUCTS.CODE))
                    .join(RECEIPTS).on(POSITIONS.RECEIPT_ID.eq(RECEIPTS.ID))
                    .where(RECEIPTS.DATE.ge(begin).and(RECEIPTS.DATE.le(end)))
                    .groupBy(PRODUCTS.CODE)
                    .fetch()
                    .collect(Collectors.toMap(record -> record.into(PRODUCTS),
                            record -> record.get(AVERAGE_PRICE).doubleValue()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Map.of();
    }

    private static final Field<Integer> FILTERED_PRODUCT_CODE = PRODUCTS.CODE.as("filtered_product_code");
    private static final Field<String> FILTERED_PRODUCT_NAME = PRODUCTS.NAME.as("filtered_product_name");

    public @NotNull Map<@NotNull OrganizationsRecord, @NotNull List<@NotNull ProductsRecord>> getSuppliedProductsInPeriod(@NotNull LocalDate begin, @Nullable LocalDate end) {
        if (end == null)
            end = begin;
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            var filtered =
                    ctx.select(ORGANIZATIONS.INN, ORGANIZATIONS.NAME, ORGANIZATIONS.GIRO,
                                    FILTERED_PRODUCT_CODE, FILTERED_PRODUCT_NAME)
                            .from(ORGANIZATIONS)
                            .join(RECEIPTS).on(RECEIPTS.ORGANIZATION_ID.eq(ORGANIZATIONS.INN))
                            .join(POSITIONS).on(POSITIONS.RECEIPT_ID.eq(RECEIPTS.ID))
                            .join(PRODUCTS).on(PRODUCTS.CODE.eq(POSITIONS.PRODUCT_ID))
                            .where(RECEIPTS.DATE.ge(begin).and(RECEIPTS.DATE.le(end)))
                            .asTable(FILTERED);
           var select =
                   ctx.select(ORGANIZATIONS.INN, ORGANIZATIONS.NAME, ORGANIZATIONS.GIRO,
                                   filtered.field(FILTERED_PRODUCT_CODE), filtered.field(FILTERED_PRODUCT_NAME))
                   .from(ORGANIZATIONS)
                   .leftOuterJoin(filtered).on(FILTERED_INN.eq(ORGANIZATIONS.INN))
                   .fetch();
           return select.collect(
                   Collectors.groupingBy(e -> e.into(ORGANIZATIONS),
                           Collectors.filtering(e -> e.get(FILTERED_PRODUCT_CODE) != null && e.get(FILTERED_PRODUCT_NAME) != null,
                                   Collectors.mapping(e -> new ProductsRecord(e.get(FILTERED_PRODUCT_CODE), e.get(FILTERED_PRODUCT_NAME)),
                                           Collectors.toList()))));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Map.of();
    }

    private final static Table<Record> TEMPORARY_DATES = DSL.table("temporary_dates");
    private final static Field<Object> TEMPORARY_DATES_FIELD = DSL.field("temporary_dates.date");
    private final static String GENERATE_TEMPORARY_DATES_SQL = "generate_series({0}::date,{1}::date,'1 day'::interval)";

    public @NotNull ProductsReport getProductsInfoInPeriod(@NotNull LocalDate begin, @Nullable LocalDate end) {
        if (end == null)
            end = begin;
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            var filtered =
                    ctx.select(PRODUCTS.CODE, PRODUCTS.NAME, POSITIONS.PRICE, POSITIONS.AMOUNT, RECEIPTS.DATE)
                            .from(PRODUCTS)
                            .join(POSITIONS).on(POSITIONS.PRODUCT_ID.eq(PRODUCTS.CODE))
                            .join(RECEIPTS).on(RECEIPTS.ID.eq(POSITIONS.RECEIPT_ID))
                            .where(RECEIPTS.DATE.ge(begin).and(RECEIPTS.DATE.le(end)))
                            .orderBy(RECEIPTS.DATE.asc())
                            .asTable(FILTERED);
            var tempDates = DSL.table(GENERATE_TEMPORARY_DATES_SQL,
                    begin, end).as(TEMPORARY_DATES.getName(), TEMPORARY_DATES_FIELD.getName());
            var select =
                    ctx.select(filtered.field(PRODUCTS.CODE),
                                    filtered.field(PRODUCTS.NAME),
                                    filtered.field(POSITIONS.PRICE),
                                    filtered.field(POSITIONS.AMOUNT),
                                    TEMPORARY_DATES_FIELD)
                            .from(tempDates)
                            .leftOuterJoin(filtered).on(TEMPORARY_DATES_FIELD.eq(filtered.field(RECEIPTS.DATE)))
                            .orderBy(TEMPORARY_DATES_FIELD.asc()).fetch();
            var perDay = select.collect(
                    Collectors.groupingBy(e -> e.get(TEMPORARY_DATES_FIELD, Date.class).toLocalDate(),
                    Collectors.filtering(e -> e.get(PRODUCTS.CODE) != null && e.get(PRODUCTS.NAME) != null
                                    && e.get(POSITIONS.PRICE) != null && e.get(POSITIONS.AMOUNT) != null,
                            Collectors.toMap(e -> e.into(PRODUCTS),
                                    e -> new ProductSummary(e.into(POSITIONS))))));
            var inPeriod = select.collect(
                    Collectors.filtering(e -> e.get(PRODUCTS.CODE) != null && e.get(PRODUCTS.NAME) != null
                                    && e.get(POSITIONS.PRICE) != null && e.get(POSITIONS.AMOUNT) != null,
                            Collectors.groupingBy(e -> e.into(PRODUCTS),
                                    Collectors.mapping(e -> new ProductSummary(e.into(POSITIONS)),
                                            Collector.of(ProductSummary::new, ProductSummary::add, ProductSummary::new)))));
            return new ProductsReport(perDay, inPeriod);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ProductsReport();
    }
}
