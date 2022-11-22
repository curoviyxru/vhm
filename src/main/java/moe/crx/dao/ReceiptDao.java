package moe.crx.dao;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.database.HikariConnectable;
import moe.crx.dto.Receipt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static moe.crx.jooq.Tables.RECEIPTS;

public final class ReceiptDao extends HikariConnectable implements IDao<Receipt> {

    @Inject
    public ReceiptDao(@NotNull HikariDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public @Nullable Receipt read(int id) {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return Optional.ofNullable(ctx.selectFrom(RECEIPTS).where(RECEIPTS.ID.eq(id)).fetchOne())
                    .map(Receipt::new).orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public @NotNull List<@NotNull Receipt> all() {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return ctx.selectFrom(RECEIPTS).fetch().map(Receipt::new);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public boolean create(@NotNull Receipt item) {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return ctx.insertInto(RECEIPTS, RECEIPTS.ID, RECEIPTS.DATE, RECEIPTS.ORGANIZATION_ID)
                    .values(item.getId(), item.getDate().toLocalDate(), item.getOrganizationId())
                    .execute() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(@NotNull Receipt item) {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return ctx.update(RECEIPTS)
                    .set(RECEIPTS.DATE, item.getDate().toLocalDate())
                    .set(RECEIPTS.ORGANIZATION_ID, item.getOrganizationId())
                    .where(RECEIPTS.ID.eq(item.getId()))
                    .execute() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(@NotNull Receipt item) {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return ctx.deleteFrom(RECEIPTS).where(RECEIPTS.ID.eq(item.getId())).execute() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
