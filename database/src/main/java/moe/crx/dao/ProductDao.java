package moe.crx.dao;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.dto.Product;
import moe.crx.jooq.tables.records.ProductsRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static moe.crx.jooq.Tables.PRODUCTS;

public final class ProductDao extends AbstractDao<Product, ProductsRecord, Integer> {

    @Inject
    public ProductDao(@NotNull HikariDataSource dataSource) {
        super(Product.class, dataSource, PRODUCTS, PRODUCTS.ID, true, PRODUCTS.NAME);
    }

    public @Nullable Product readByName(String name) {
        try (var c = getConnection()) {
            return c.context()
                    .fetchOptional(PRODUCTS, PRODUCTS.NAME.eq(name))
                    .map(r -> r.into(Product.class))
                    .orElse(null);
        }
    }

    public @NotNull List<@NotNull Product> allByOrgId(int id) {
        try (var c = getConnection()) {
            return c.context()
                    .fetch(PRODUCTS, PRODUCTS.ORG_ID.eq(id))
                    .into(Product.class);
        }
    }
}
