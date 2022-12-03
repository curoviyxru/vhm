package moe.crx.tests.dao;

import moe.crx.dao.ProductDao;
import moe.crx.jooq.tables.records.ProductsRecord;
import moe.crx.tests.GuicedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public final class ProductDaoTest extends GuicedTest {

    @Test
    void read() {
        var dao = injector.getInstance(ProductDao.class);
        var entries = Arrays.asList(
                new ProductsRecord(1, "Kokia 10.9 6G"),
                new ProductsRecord(2, "ePhone 14 Super Puper"),
                new ProductsRecord(3, "Ximi Reimu 11T"),
                new ProductsRecord(4, "Gnusmas Space C22")
        );
        for (ProductsRecord product : entries) {
            assertEquals(dao.read(product.getCode()), product);
        }
    }

    @Test
    void all() {
        var dao = injector.getInstance(ProductDao.class);
        var entries = Arrays.asList(
                new ProductsRecord(1, "Kokia 10.9 6G"),
                new ProductsRecord(2, "ePhone 14 Super Puper"),
                new ProductsRecord(3, "Ximi Reimu 11T"),
                new ProductsRecord(4, "Gnusmas Space C22")
        );
        var actual = dao.all();

        assertTrue(actual.size() == entries.size() && actual.containsAll(entries));
    }

    @Test
    void create() {
        var dao = injector.getInstance(ProductDao.class);
        var product = new ProductsRecord(random.nextInt(), "Test Product");
        assertTrue(dao.create(product));
        assertEquals(product, dao.read(product.getCode()));
        assertTrue(dao.delete(product));
    }

    @Test
    void update() {
        var dao = injector.getInstance(ProductDao.class);
        var product = new ProductsRecord(random.nextInt(), "Test Product");
        assertTrue(dao.create(product));
        assertEquals(product, dao.read(product.getCode()));
        product = new ProductsRecord(product.getCode(), "Test Product 2");
        assertTrue(dao.update(product));
        assertEquals(product, dao.read(product.getCode()));
        assertTrue(dao.delete(product));
    }

    @Test
    void delete() {
        var dao = injector.getInstance(ProductDao.class);
        var product = new ProductsRecord(random.nextInt(), "Test Product");
        assertTrue(dao.create(product));
        assertEquals(product, dao.read(product.getCode()));
        assertTrue(dao.delete(product));
        assertNull(dao.read(product.getCode()));
    }
}
