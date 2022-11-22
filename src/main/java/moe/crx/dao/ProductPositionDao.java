package moe.crx.dao;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.database.HikariConnectable;
import moe.crx.dto.ProductPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static moe.crx.jooq.Tables.POSITIONS;

public final class ProductPositionDao extends HikariConnectable implements IDao<ProductPosition> {

    @Inject
    public ProductPositionDao(@NotNull HikariDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public @Nullable ProductPosition read(int id) {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return Optional.ofNullable(ctx.selectFrom(POSITIONS).where(POSITIONS.ID.eq(id)).fetchOne())
                    .map(ProductPosition::new).orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public @NotNull List<@NotNull ProductPosition> all() {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return ctx.selectFrom(POSITIONS).fetch().map(ProductPosition::new);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public boolean create(@NotNull ProductPosition item) {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return ctx.insertInto(POSITIONS, POSITIONS.ID, POSITIONS.RECEIPT_ID,
                            POSITIONS.PRODUCT_ID, POSITIONS.PRICE, POSITIONS.AMOUNT)
                    .values(item.getId(), item.getReceiptId(), item.getProductId(), item.getPrice(), item.getAmount())
                    .execute() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(@NotNull ProductPosition item) {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return ctx.update(POSITIONS)
                    .set(POSITIONS.RECEIPT_ID, item.getReceiptId())
                    .set(POSITIONS.PRODUCT_ID, item.getProductId())
                    .set(POSITIONS.PRICE, item.getPrice())
                    .set(POSITIONS.AMOUNT, item.getAmount())
                    .where(POSITIONS.ID.eq(item.getId()))
                    .execute() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(@NotNull ProductPosition item) {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return ctx.deleteFrom(POSITIONS).where(POSITIONS.ID.eq(item.getId())).execute() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
