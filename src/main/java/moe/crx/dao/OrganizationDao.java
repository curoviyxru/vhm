package moe.crx.dao;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.database.HikariConnectable;
import moe.crx.dto.Organization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class OrganizationDao extends HikariConnectable implements IDao<Organization> {

    private static final String SELECT_SQL = "SELECT * FROM organizations WHERE inn = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM organizations";
    private static final String INSERT_SQL = "INSERT INTO organizations (inn, name, giro) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE organizations SET name = ?, giro = ? WHERE inn = ?";
    private static final String DELETE_SQL = "DELETE FROM organizations WHERE inn = ?";

    @Inject
    public OrganizationDao(@NotNull HikariDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public @Nullable Organization read(int id) {
        try (var connection = getConnection();
             var statement = connection.prepareStatement(SELECT_SQL)) {
            statement.setInt(1, id);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Organization(
                            resultSet.getInt("inn"),
                            resultSet.getString("name"),
                            resultSet.getString("giro")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public @NotNull List<@NotNull Organization> all() {
        final var result = new ArrayList<Organization>();
        try (var connection = getConnection();
             var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(SELECT_ALL_SQL)) {
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

    @Override
    public boolean create(@NotNull Organization item) {
        try (var connection = getConnection();
             var statement = connection.prepareStatement(INSERT_SQL)) {
            int i = 1;
            statement.setInt(i++, item.getInn());
            statement.setString(i++, item.getName());
            statement.setString(i, item.getGiro());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(@NotNull Organization item) {
        try (var connection = getConnection();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            int i = 1;
            statement.setString(i++, item.getName());
            statement.setString(i++, item.getGiro());
            statement.setInt(i, item.getInn());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(@NotNull Organization item) {
        try (var connection = getConnection();
             var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, item.getInn());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
