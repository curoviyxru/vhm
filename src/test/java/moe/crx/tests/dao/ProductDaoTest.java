package moe.crx.tests.dao;

import moe.crx.dao.ProductDao;
import moe.crx.dto.Product;
import moe.crx.tests.GuicedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public final class ProductDaoTest extends GuicedTest {

    @Test
    void read() {
        var dao = injector.getInstance(ProductDao.class);
        var entries = Arrays.asList(
                new Product(1, "Kokia 10.9 6G"),
                new Product(2, "ePhone 14 Super Puper"),
                new Product(3, "Ximi Reimu 11T"),
                new Product(4, "Gnusmas Space C22")
        );
        for (Product product : entries) {
            assertEquals(dao.read(product.getCode()), product);
        }
    }

    @Test
    void all() {
        var dao = injector.getInstance(ProductDao.class);
        var entries = Arrays.asList(
                new Product(1, "Kokia 10.9 6G"),
                new Product(2, "ePhone 14 Super Puper"),
                new Product(3, "Ximi Reimu 11T"),
                new Product(4, "Gnusmas Space C22")
        );
        assertEquals(entries, dao.all());
    }

    @Test
    void create() {
        var dao = injector.getInstance(ProductDao.class);
        var product = new Product(random.nextInt(), "Test Product");
        assertTrue(dao.create(product));
        assertEquals(product, dao.read(product.getCode()));
        assertTrue(dao.delete(product));
    }

    @Test
    void update() {
        var dao = injector.getInstance(ProductDao.class);
        var product = new Product(random.nextInt(), "Test Product");
        assertTrue(dao.create(product));
        assertEquals(product, dao.read(product.getCode()));
        product = new Product(product.getCode(), "Test Product 2");
        assertTrue(dao.update(product));
        assertEquals(product, dao.read(product.getCode()));
        assertTrue(dao.delete(product));
    }

    @Test
    void delete() {
        var dao = injector.getInstance(ProductDao.class);
        var product = new Product(random.nextInt(), "Test Product");
        assertTrue(dao.create(product));
        assertEquals(product, dao.read(product.getCode()));
        assertTrue(dao.delete(product));
        assertNull(dao.read(product.getCode()));
    }
}
