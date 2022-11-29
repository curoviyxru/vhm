package moe.crx.dao;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.database.HikariConnectable;
import moe.crx.dto.Organization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static moe.crx.jooq.Tables.ORGANIZATIONS;

public final class OrganizationDao extends HikariConnectable implements IDao<Organization> {

    @Inject
    public OrganizationDao(@NotNull HikariDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public @Nullable Organization read(int id) {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return Optional.ofNullable(ctx.selectFrom(ORGANIZATIONS).where(ORGANIZATIONS.ID.eq(id)).fetchOne())
                    .map(Organization::new).orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public @NotNull List<@NotNull Organization> all() {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return ctx.selectFrom(ORGANIZATIONS).fetch().map(Organization::new);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public int create(@NotNull Organization item) {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            ctx.insertInto(ORGANIZATIONS, ORGANIZATIONS.NAME)
                    .values(item.getName())
                    .onDuplicateKeyIgnore()
                    .execute();
            return Objects.requireNonNull(
                    ctx.selectFrom(ORGANIZATIONS).where(ORGANIZATIONS.NAME.eq(item.getName())).fetchOne()
            ).getId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean update(@NotNull Organization item) {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return ctx.update(ORGANIZATIONS)
                    .set(ORGANIZATIONS.NAME, item.getName())
                    .where(ORGANIZATIONS.ID.eq(item.getId()))
                    .execute() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(@NotNull Organization item) {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return ctx.deleteFrom(ORGANIZATIONS).where(ORGANIZATIONS.ID.eq(item.getId())).execute() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
