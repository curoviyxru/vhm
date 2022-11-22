package moe.crx.database;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.dto.Organization;
import moe.crx.dto.Product;
import moe.crx.dto.reports.ProductSummary;
import moe.crx.dto.reports.ProductsReport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Reporter extends HikariConnectable {

    @Inject
    public Reporter(@NotNull HikariDataSource dataSource) {
        super(dataSource);
    }

    private static final String GET_TOP_SUPPLIERS_SQL = """
                    SELECT organizations.inn, organizations.name, organizations.giro FROM organizations
                    LEFT OUTER JOIN
                    (SELECT organizations.inn, organizations.name, organizations.giro, SUM(positions.amount) as amount FROM organizations
                    JOIN receipts ON receipts.organization_id=organizations.inn
                    JOIN positions ON receipts.id=positions.receipt_id
                    GROUP BY organizations.inn
                    ORDER BY SUM(positions.amount) DESC LIMIT 10) filtered ON filtered.inn=organizations.inn
                    ORDER BY filtered.amount DESC NULLS LAST LIMIT 10""";

    public @NotNull List<@NotNull Organization> getTopSuppliers() {
        final var result = new ArrayList<Organization>();
        try (var connection = getConnection();
             var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(GET_TOP_SUPPLIERS_SQL)) {
                while (resultSet.next()) {
                    result.add(new Organization(
                            resultSet.getInt("inn"),
                            resultSet.getString("name"),
                            resultSet.getString("giro")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final String GET_SUPPLIERS_BY_PRODUCT_AND_LIMIT = """
                    SELECT organizations.inn,organizations.name,organizations.giro from organizations
                    JOIN receipts ON receipts.organization_id=organizations.inn
                    JOIN positions ON positions.receipt_id=receipts.id WHERE""";

    public @NotNull List<@NotNull Organization> getSuppliersByProductAndLimit(@NotNull Map<@NotNull Product, @NotNull Integer> limits) {
        final var result = new ArrayList<Organization>();
        if (limits.size() == 0)
            return result;
        var builder = new StringBuilder();
        builder.append(GET_SUPPLIERS_BY_PRODUCT_AND_LIMIT);
        builder.append(" (product_id = ? AND amount >= ?) OR".repeat(limits.size()));
        builder.setLength(builder.length() - 3);
        builder.append(" GROUP BY organizations.inn HAVING COUNT(*) >= ?");
        try (var connection = getConnection();
             var statement = connection.prepareStatement(builder.toString())) {
            int i = 1;
            for (Map.Entry<Product, Integer> entry : limits.entrySet()) {
                statement.setInt(i++, entry.getKey().getCode());
                statement.setInt(i++, entry.getValue());
            }
            statement.setInt(i, limits.size());
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(new Organization(
                            resultSet.getInt("inn"),
                            resultSet.getString("name"),
                            resultSet.getString("giro")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final String GET_AVERAGE_PRICE_IN_PERIOD = """
            SELECT code, name, AVG(price) FROM products
            JOIN positions ON positions.product_id=products.code
            JOIN receipts ON positions.receipt_id=receipts.id
            WHERE date >= ? AND date <= ?
            GROUP BY code""";

    public @NotNull Map<@NotNull Product, @NotNull Double> getAveragePriceInPeriod(@NotNull Date begin, @Nullable Date end) {
        if (end == null)
            end = begin;
        final var result = new HashMap<Product, Double>();
        try (var connection = getConnection();
             var statement = connection.prepareStatement(GET_AVERAGE_PRICE_IN_PERIOD)) {
            statement.setDate(1, begin);
            statement.setDate(2, end);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.put(new Product(resultSet.getInt("code"), resultSet.getString("name")), resultSet.getDouble("avg"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final String GET_SUPPLIED_PRODUCTS_IN_PERIOD = """
            SELECT organizations.inn, organizations.name, organizations.giro, filtered.code AS product_code, filtered.name AS product_name FROM organizations
            LEFT OUTER JOIN
            (SELECT organizations.inn, products.code, products.name FROM organizations
            JOIN receipts ON receipts.organization_id=organizations.inn
            JOIN positions ON positions.receipt_id=receipts.id
            JOIN products ON products.code=positions.product_id
            WHERE receipts.date >= ? AND receipts.date <= ?) filtered
            ON filtered.inn=organizations.inn""";

    public @NotNull Map<@NotNull Organization, @NotNull List<@NotNull Product>> getSuppliedProductsInPeriod(@NotNull Date begin, @Nullable Date end) {
        if (end == null)
            end = begin;
        final var result = new HashMap<Organization, List<Product>>();
        try (var connection = getConnection();
             var statement = connection.prepareStatement(GET_SUPPLIED_PRODUCTS_IN_PERIOD)) {
            statement.setDate(1, begin);
            statement.setDate(2, end);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var org = new Organization(resultSet.getInt("inn"), resultSet.getString("name"), resultSet.getString("giro"));
                    var product = new Product(resultSet.getInt("product_code"), resultSet.getString("product_name"));
                    var products = result.getOrDefault(org, new ArrayList<>());
                    if (!resultSet.wasNull())
                        products.add(product);
                    result.put(org, products);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final String PREPARE_TEMPORARY_DATES = """
            DROP TABLE IF EXISTS temporary_dates;
            CREATE TEMPORARY TABLE temporary_dates (date date NOT NULL);
            INSERT INTO temporary_dates select generate_series(?,?,'1 day'::interval);""";
    private static final String GET_PRODUCTS_INFO_IN_PERIOD = """
            SELECT filtered.code, filtered.name, filtered.price, filtered.amount, temporary_dates.date FROM temporary_dates
            LEFT OUTER JOIN (SELECT products.code, products.name, positions.price, positions.amount, receipts.date FROM products
                        JOIN positions ON positions.product_id=products.code
                        JOIN receipts ON receipts.id=positions.receipt_id
                        WHERE date >= ? AND date <= ?
                        ORDER BY date ASC) filtered on filtered.date = temporary_dates.date;""";
    private static final String DROP_TEMPORARY_DATES = """
            DROP TABLE IF EXISTS temporary_dates;""";

    public @NotNull ProductsReport getProductsInfoInPeriod(@NotNull Date begin, @Nullable Date end) {
        if (end == null)
            end = begin;
        final var result = new ProductsReport();
        try (var connection = getConnection()) {
            try (var statement = connection.prepareStatement(PREPARE_TEMPORARY_DATES)) {
                statement.setDate(1, begin);
                statement.setDate(2, end);
                statement.executeUpdate();
            }
            try (var statement = connection.prepareStatement(GET_PRODUCTS_INFO_IN_PERIOD)) {
                statement.setDate(1, begin);
                statement.setDate(2, end);
                try (var resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        var date = resultSet.getDate("date");
                        var product = new Product(resultSet.getInt("code"), resultSet.getString("name"));
                        var summary = new ProductSummary(resultSet.getInt("amount"), resultSet.getInt("amount")*resultSet.getInt("price"));
                        var perDay = result.getPerDay().getOrDefault(date, new HashMap<>());
                        if (!resultSet.wasNull())
                            perDay.put(product, summary);
                        result.getPerDay().put(date, perDay);
                        if (resultSet.wasNull())
                            continue;
                        var inPeriod = result.getInPeriod().getOrDefault(product, new ProductSummary());
                        inPeriod.setAmount(inPeriod.getAmount() + summary.getAmount());
                        inPeriod.setSum(inPeriod.getSum() + summary.getSum());
                        result.getInPeriod().put(product, inPeriod);
                    }
                }
            }
            try (var statement = connection.prepareStatement(DROP_TEMPORARY_DATES)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
