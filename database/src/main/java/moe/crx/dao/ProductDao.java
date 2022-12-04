package moe.crx.dao;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.jooq.tables.records.ProductsRecord;
import org.jetbrains.annotations.NotNull;

import static moe.crx.jooq.Tables.PRODUCTS;

public final class ProductDao extends AbstractDao<ProductsRecord, Integer> {

    @Inject
    public ProductDao(@NotNull HikariDataSource dataSource) {
        super(dataSource, PRODUCTS, PRODUCTS.ID, true);
    }
}
