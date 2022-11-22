package moe.crx.dao;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.database.HikariConnectable;
import moe.crx.dto.Product;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ProductDao extends HikariConnectable implements IDao<Product> {

    private static final String SELECT_SQL = "SELECT * FROM products WHERE code = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM products";
    private static final String INSERT_SQL = "INSERT INTO products (code, name) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE products SET name = ? WHERE code = ?";
    private static final String DELETE_SQL = "DELETE FROM products WHERE code = ?";

    @Inject
    public ProductDao(@NotNull HikariDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public @Nullable Product read(int id) {
        try (var connection = getConnection();
             var statement = connection.prepareStatement(SELECT_SQL)) {
            statement.setInt(1, id);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Product(
                            resultSet.getInt("code"),
                            resultSet.getString("name")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public @NotNull List<@NotNull Product> all() {
        final var result = new ArrayList<Product>();
        try (var connection = getConnection();
             var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(SELECT_ALL_SQL)) {
                while (resultSet.next()) {
                    result.add(new Product(
                            resultSet.getInt("code"),
                            resultSet.getString("name")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean create(@NotNull Product item) {
        try (var connection = getConnection();
             var statement = connection.prepareStatement(INSERT_SQL)) {
            int i = 1;
            statement.setInt(i++, item.getCode());
            statement.setString(i, item.getName());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(@NotNull Product item) {
        try (var connection = getConnection();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            int i = 1;
            statement.setString(i++, item.getName());
            statement.setInt(i, item.getCode());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(@NotNull Product item) {
        try (var connection = getConnection();
             var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, item.getCode());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
