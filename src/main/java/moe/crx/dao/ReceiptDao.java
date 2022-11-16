package moe.crx.dao;

import com.google.inject.Inject;
import moe.crx.dto.Receipt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ReceiptDao extends AbstractDao<Receipt> {
    private static final String SELECT_SQL = "SELECT * FROM receipts WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM receipts";
    private static final String INSERT_SQL = "INSERT INTO receipts (id, date, organization_id) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE receipts SET date = ?, organization_id = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM receipts WHERE id = ?";

    @Inject
    public ReceiptDao(@NotNull Connection connection) {
        super(connection);
    }

    @Override
    public @Nullable Receipt read(int id) {
        try (var statement = getConnection().prepareStatement(SELECT_SQL)) {
            statement.setInt(1, id);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Receipt(
                            resultSet.getInt("id"),
                            resultSet.getDate("date"),
                            resultSet.getInt("organization_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public @NotNull List<@NotNull Receipt> all() {
        final var result = new ArrayList<Receipt>();
        try (var statement = getConnection().createStatement()) {
            try (var resultSet = statement.executeQuery(SELECT_ALL_SQL)) {
                while (resultSet.next()) {
                    result.add(new Receipt(
                            resultSet.getInt("id"),
                            resultSet.getDate("date"),
                            resultSet.getInt("organization_id")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean create(@NotNull Receipt item) {
        try (var statement = getConnection().prepareStatement(INSERT_SQL)) {
            int i = 1;
            statement.setInt(i++, item.getId());
            statement.setDate(i++, item.getDate());
            statement.setInt(i, item.getOrganizationId());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(@NotNull Receipt item) {
        try (var statement = getConnection().prepareStatement(UPDATE_SQL)) {
            int i = 1;
            statement.setDate(i++, item.getDate());
            statement.setInt(i++, item.getOrganizationId());
            statement.setInt(i, item.getId());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(@NotNull Receipt item) {
        try (var statement = getConnection().prepareStatement(DELETE_SQL)) {
            statement.setInt(1, item.getId());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
