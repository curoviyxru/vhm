package moe.crx.dao;

import com.google.inject.Inject;
import moe.crx.dto.ProductPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ProductPositionDao extends AbstractDao<ProductPosition> {
    private static final String SELECT_SQL = "SELECT * FROM positions WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM positions";
    private static final String INSERT_SQL = "INSERT INTO positions (id, receipt_id, product_id, price, amount) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE positions SET receipt_id = ?, product_id = ?, price = ?, amount = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM positions WHERE id = ?";

    @Inject
    public ProductPositionDao(@NotNull Connection connection) {
        super(connection);
    }

    @Override
    public @Nullable ProductPosition read(int id) {
        try (var statement = getConnection().prepareStatement(SELECT_SQL)) {
            statement.setInt(1, id);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new ProductPosition(
                            resultSet.getInt("id"),
                            resultSet.getInt("receipt_id"),
                            resultSet.getInt("product_id"),
                            resultSet.getDouble("price"),
                            resultSet.getInt("amount")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public @NotNull List<@NotNull ProductPosition> all() {
        final var result = new ArrayList<ProductPosition>();
        try (var statement = getConnection().createStatement()) {
            try (var resultSet = statement.executeQuery(SELECT_ALL_SQL)) {
                while (resultSet.next()) {
                    result.add(new ProductPosition(
                            resultSet.getInt("id"),
                            resultSet.getInt("receipt_id"),
                            resultSet.getInt("product_id"),
                            resultSet.getDouble("price"),
                            resultSet.getInt("amount")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean create(@NotNull ProductPosition item) {
        try (var statement = getConnection().prepareStatement(INSERT_SQL)) {
            int i = 1;
            statement.setInt(i++, item.getId());
            statement.setInt(i++, item.getReceiptId());
            statement.setInt(i++, item.getProductId());
            statement.setDouble(i++, item.getPrice());
            statement.setInt(i, item.getAmount());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(@NotNull ProductPosition item) {
        try (var statement = getConnection().prepareStatement(UPDATE_SQL)) {
            int i = 1;
            statement.setInt(i++, item.getReceiptId());
            statement.setInt(i++, item.getProductId());
            statement.setDouble(i++, item.getPrice());
            statement.setInt(i++, item.getAmount());
            statement.setInt(i, item.getId());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(@NotNull ProductPosition item) {
        try (var statement = getConnection().prepareStatement(DELETE_SQL)) {
            statement.setInt(1, item.getId());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
