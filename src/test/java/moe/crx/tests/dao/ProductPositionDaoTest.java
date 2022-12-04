package moe.crx.tests.dao;

import moe.crx.dao.ProductPositionDao;
import moe.crx.jooq.tables.records.PositionsRecord;
import moe.crx.tests.GuiceTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public final class ProductPositionDaoTest extends GuiceTest<ProductPositionDao> {

    public ProductPositionDaoTest() {
        super(ProductPositionDao.class);
    }

    @Test
    void read() {
        var entries = Arrays.asList(
                new PositionsRecord(3, 8, 2, 63999.0, 500),
                new PositionsRecord(2, 7, 4, 67700.0, 250),
                new PositionsRecord(1, 2, 2, 59999.99, 400),
                new PositionsRecord(10, 6, 1, 34999.99, 300),
                new PositionsRecord(9, 1, 3, 19999.99, 1000),
                new PositionsRecord(8, 5, 4, 54999.9, 560),
                new PositionsRecord(11, 3, 2, 69999.99, 100),
                new PositionsRecord(6, 3, 4, 69999.99, 50),
                new PositionsRecord(5, 2, 2, 59999.99, 1000),
                new PositionsRecord(4, 5, 3, 21999.0, 200)
        );
        for (PositionsRecord product : entries) {
            assertEquals(instance.read(product.getId()), product);
        }
    }

    @Test
    void all() {
        var entries = Arrays.asList(
                new PositionsRecord(3, 8, 2, 63999.0, 500),
                new PositionsRecord(2, 7, 4, 67700.0, 250),
                new PositionsRecord(1, 2, 2, 59999.99, 400),
                new PositionsRecord(10, 6, 1, 34999.99, 300),
                new PositionsRecord(9, 1, 3, 19999.99, 1000),
                new PositionsRecord(8, 5, 4, 54999.9, 560),
                new PositionsRecord(11, 3, 2, 69999.99, 100),
                new PositionsRecord(6, 3, 4, 69999.99, 50),
                new PositionsRecord(5, 2, 2, 59999.99, 1000),
                new PositionsRecord(4, 5, 3, 21999.0, 200)
        );
        var actual = instance.all();

        assertTrue(actual.size() == entries.size() && actual.containsAll(entries));
    }

    @Test
    void create() {
        var product = new PositionsRecord(random.nextInt(), 1, 1, 19999.99, 100);
        assertNotNull(instance.create(product));
        assertEquals(product, instance.read(product.getId()));
        assertTrue(instance.delete(product));
    }

    @Test
    void update() {
        var product = new PositionsRecord(random.nextInt(), 1, 1, 19999.99, 100);
        assertNotNull(instance.create(product));
        assertEquals(product, instance.read(product.getId()));
        product = new PositionsRecord(product.getId(), 3, 3, 39999.99, 300);
        assertTrue(instance.update(product));
        assertEquals(product, instance.read(product.getId()));
        assertTrue(instance.delete(product));
    }

    @Test
    void delete() {
        var product = new PositionsRecord(random.nextInt(), 1, 1, 19999.99, 100);
        assertNotNull(instance.create(product));
        assertEquals(product, instance.read(product.getId()));
        assertTrue(instance.delete(product));
        assertNull(instance.read(product.getId()));
    }
}
