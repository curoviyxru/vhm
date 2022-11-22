package moe.crx.database;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.dto.Organization;
import moe.crx.dto.Product;
import moe.crx.dto.reports.ProductSummary;
import moe.crx.dto.reports.ProductsReport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Date;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static moe.crx.jooq.Tables.*;

public final class Reporter extends HikariConnectable {

    @Inject
    public Reporter(@NotNull HikariDataSource dataSource) {
        super(dataSource);
    }

    public @NotNull List<@NotNull Organization> getTopSuppliers() {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            var filtered =
                    ctx.select(ORGANIZATIONS.INN, ORGANIZATIONS.NAME, ORGANIZATIONS.GIRO, DSL.sum(POSITIONS.AMOUNT).as("amount"))
                            .from(ORGANIZATIONS)
                            .join(RECEIPTS).on(RECEIPTS.ORGANIZATION_ID.eq(ORGANIZATIONS.INN))
                            .join(POSITIONS).on(RECEIPTS.ID.eq(POSITIONS.RECEIPT_ID))
                            .groupBy(ORGANIZATIONS.INN)
                            .orderBy(DSL.sum(POSITIONS.AMOUNT).desc())
                            .limit(10)
                            .asTable("filtered");
            return ctx.select(ORGANIZATIONS.INN, ORGANIZATIONS.NAME, ORGANIZATIONS.GIRO)
                    .from(ORGANIZATIONS)
                    .leftOuterJoin(filtered).on(DSL.field("filtered.inn").eq(ORGANIZATIONS.INN))
                    .orderBy(DSL.field("filtered.amount").desc().nullsLast())
                    .limit(10)
                    .fetch()
                    .map(record -> new Organization(record.into(ORGANIZATIONS)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public @NotNull List<@NotNull Organization> getSuppliersByProductAndLimit(@NotNull Map<@NotNull Product, @NotNull Integer> limits) {
        if (limits.size() == 0)
            return List.of();
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            Condition where = DSL.noCondition();
            for (Map.Entry<Product, Integer> entry : limits.entrySet()) {
                where = where.or(POSITIONS.PRODUCT_ID.eq(entry.getKey().getCode()).and(POSITIONS.AMOUNT.ge(entry.getValue())));
            }
            return ctx.select(ORGANIZATIONS.INN, ORGANIZATIONS.NAME, ORGANIZATIONS.GIRO).from(ORGANIZATIONS)
                    .join(RECEIPTS).on(RECEIPTS.ORGANIZATION_ID.eq(ORGANIZATIONS.INN))
                    .join(POSITIONS).on(POSITIONS.RECEIPT_ID.eq(RECEIPTS.ID))
                    .where(where)
                    .groupBy(ORGANIZATIONS.INN)
                    .having(DSL.count().ge(limits.size()))
                    .fetch()
                    .map(record -> new Organization(record.into(ORGANIZATIONS)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public @NotNull Map<@NotNull Product, @NotNull Double> getAveragePriceInPeriod(@NotNull Date begin, @Nullable Date end) {
        if (end == null)
            end = begin;
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return ctx.select(PRODUCTS.CODE, PRODUCTS.NAME, DSL.avg(POSITIONS.PRICE).as("avg")).from(PRODUCTS)
                    .join(POSITIONS).on(POSITIONS.PRODUCT_ID.eq(PRODUCTS.CODE))
                    .join(RECEIPTS).on(POSITIONS.RECEIPT_ID.eq(RECEIPTS.ID))
                    .where(RECEIPTS.DATE.ge(begin.toLocalDate()).and(RECEIPTS.DATE.le(end.toLocalDate())))
                    .groupBy(PRODUCTS.CODE)
                    .fetch()
                    .collect(Collectors.toMap(record -> new Product(record.into(PRODUCTS)),
                            record -> record.get("avg", Double.class)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Map.of();
    }

    public @NotNull Map<@NotNull Organization, @NotNull List<@NotNull Product>> getSuppliedProductsInPeriod(@NotNull Date begin, @Nullable Date end) {
        if (end == null)
            end = begin;
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            var result = new HashMap<Organization, List<Product>>();
            var filtered =
                    ctx.select(ORGANIZATIONS.INN, ORGANIZATIONS.NAME, ORGANIZATIONS.GIRO,
                                    PRODUCTS.CODE.as("product_code"), PRODUCTS.NAME.as("product_name"))
                            .from(ORGANIZATIONS)
                            .join(RECEIPTS).on(RECEIPTS.ORGANIZATION_ID.eq(ORGANIZATIONS.INN))
                            .join(POSITIONS).on(POSITIONS.RECEIPT_ID.eq(RECEIPTS.ID))
                            .join(PRODUCTS).on(PRODUCTS.CODE.eq(POSITIONS.PRODUCT_ID))
                            .where(RECEIPTS.DATE.ge(begin.toLocalDate()).and(RECEIPTS.DATE.le(end.toLocalDate())))
                            .asTable("filtered");
           var select =
                   ctx.select(ORGANIZATIONS.INN, ORGANIZATIONS.NAME, ORGANIZATIONS.GIRO,
                           DSL.field("filtered.product_code").as("product_code"),
                           DSL.field("filtered.product_name").as("product_name"))
                   .from(ORGANIZATIONS)
                   .leftOuterJoin(filtered).on(DSL.field("filtered.inn").eq(ORGANIZATIONS.INN))
                   .fetch();
           for (var record : select) {
               var org = new Organization(record.into(ORGANIZATIONS));
               var products = result.getOrDefault(org, new ArrayList<>());
               if (record.get("product_code") != null && record.get("product_name") != null)
                   products.add(new Product(record.get("product_code", Integer.class),
                           record.get("product_name", String.class)));
               result.put(org, products);
           }
           return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Map.of();
    }

    public @NotNull ProductsReport getProductsInfoInPeriod(@NotNull Date begin, @Nullable Date end) {
        if (end == null)
            end = begin;
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            var result = new ProductsReport();
            var filtered =
                    ctx.select(PRODUCTS.CODE, PRODUCTS.NAME, POSITIONS.PRICE, POSITIONS.AMOUNT, RECEIPTS.DATE)
                            .from(PRODUCTS)
                            .join(POSITIONS).on(POSITIONS.PRODUCT_ID.eq(PRODUCTS.CODE))
                            .join(RECEIPTS).on(RECEIPTS.ID.eq(POSITIONS.RECEIPT_ID))
                            .where(RECEIPTS.DATE.ge(begin.toLocalDate()).and(RECEIPTS.DATE.le(end.toLocalDate())))
                            .orderBy(RECEIPTS.DATE.asc())
                            .asTable("filtered");
            var select =
                    ctx.select(DSL.field("filtered.code").as("code"),
                            DSL.field("filtered.name").as("name"),
                            DSL.field("filtered.price").as("price"),
                            DSL.field("filtered.amount").as("amount"),
                            DSL.field("temporary_dates.date").as("date"))
                            .from(DSL.table("generate_series({0}::date,{1}::date,'1 day'::interval)",
                                    begin.toLocalDate(), end.toLocalDate()).as("temporary_dates"))
                            .leftOuterJoin(filtered).on(DSL.field("filtered.date").eq(DSL.field("temporary_dates.date")))
                            .orderBy(DSL.field("temporary_dates.date").asc()).fetch();
            for (var record : select) {
                var date = record.get("date", Date.class);
                var perDay = result.getPerDay().getOrDefault(date, new HashMap<>());
                if (record.get("code") != null && record.get("name") != null) {
                    var product = new Product(record.get("code", Integer.class), record.get("name", String.class));
                    if (record.get("amount") != null && record.get("price") != null) {
                        var summary = new ProductSummary(record.get("amount", Integer.class),
                                record.get("price", Integer.class)*record.get("amount", Integer.class));
                        perDay.put(product, summary);
                        var inPeriod = result.getInPeriod().getOrDefault(product, new ProductSummary());
                        inPeriod.setAmount(inPeriod.getAmount() + summary.getAmount());
                        inPeriod.setSum(inPeriod.getSum() + summary.getSum());
                        result.getInPeriod().put(product, inPeriod);
                    }
                }
                result.getPerDay().put(date, perDay);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ProductsReport();
    }
}
