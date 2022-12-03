package moe.crx.tests.dao;

import moe.crx.dao.ProductDao;
import moe.crx.jooq.tables.records.ProductsRecord;
import moe.crx.tests.GuiceTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public final class ProductDaoTest extends GuiceTest<ProductDao> {

    public ProductDaoTest() {
        super(ProductDao.class);
    }

    @Test
    void read() {
        var entries = Arrays.asList(
                new ProductsRecord(1, "Kokia 10.9 6G"),
                new ProductsRecord(2, "ePhone 14 Super Puper"),
                new ProductsRecord(3, "Ximi Reimu 11T"),
                new ProductsRecord(4, "Gnusmas Space C22")
        );
        for (ProductsRecord product : entries) {
            assertEquals(instance.read(product.getCode()), product);
        }
    }

    @Test
    void all() {
        var entries = Arrays.asList(
                new ProductsRecord(1, "Kokia 10.9 6G"),
                new ProductsRecord(2, "ePhone 14 Super Puper"),
                new ProductsRecord(3, "Ximi Reimu 11T"),
                new ProductsRecord(4, "Gnusmas Space C22")
        );
        var actual = instance.all();

        assertTrue(actual.size() == entries.size() && actual.containsAll(entries));
    }

    @Test
    void create() {
        var product = new ProductsRecord(random.nextInt(), "Test Product");
        assertNotNull(instance.create(product));
        assertEquals(product, instance.read(product.getCode()));
        assertTrue(instance.delete(product));
    }

    @Test
    void update() {
        var product = new ProductsRecord(random.nextInt(), "Test Product");
        assertNotNull(instance.create(product));
        assertEquals(product, instance.read(product.getCode()));
        product = new ProductsRecord(product.getCode(), "Test Product 2");
        assertTrue(instance.update(product));
        assertEquals(product, instance.read(product.getCode()));
        assertTrue(instance.delete(product));
    }

    @Test
    void delete() {
        var product = new ProductsRecord(random.nextInt(), "Test Product");
        assertNotNull(instance.create(product));
        assertEquals(product, instance.read(product.getCode()));
        assertTrue(instance.delete(product));
        assertNull(instance.read(product.getCode()));
    }
}
