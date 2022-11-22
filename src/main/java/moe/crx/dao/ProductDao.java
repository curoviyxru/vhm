package moe.crx.dao;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.database.HikariConnectable;
import moe.crx.dto.Product;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static moe.crx.jooq.Tables.PRODUCTS;

public final class ProductDao extends HikariConnectable implements IDao<Product> {

    @Inject
    public ProductDao(@NotNull HikariDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public @Nullable Product read(int id) {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return Optional.ofNullable(ctx.selectFrom(PRODUCTS).where(PRODUCTS.CODE.eq(id)).fetchOne())
                    .map(Product::new).orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public @NotNull List<@NotNull Product> all() {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return ctx.selectFrom(PRODUCTS).fetch().map(Product::new);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public boolean create(@NotNull Product item) {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return ctx.insertInto(PRODUCTS, PRODUCTS.CODE, PRODUCTS.NAME)
                    .values(item.getCode(), item.getName())
                    .execute() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(@NotNull Product item) {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return ctx.update(PRODUCTS)
                    .set(PRODUCTS.NAME, item.getName())
                    .where(PRODUCTS.CODE.eq(item.getCode()))
                    .execute() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(@NotNull Product item) {
        try (var connection = getConnection()) {
            var ctx = DSL.using(connection, SQLDialect.POSTGRES);
            return ctx.deleteFrom(PRODUCTS).where(PRODUCTS.CODE.eq(item.getCode())).execute() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
