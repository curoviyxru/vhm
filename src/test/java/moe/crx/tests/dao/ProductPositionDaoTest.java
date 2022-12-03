package moe.crx.tests.dao;

import moe.crx.dao.ProductPositionDao;
import moe.crx.jooq.tables.records.PositionsRecord;
import moe.crx.tests.GuicedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public final class ProductPositionDaoTest extends GuicedTest {

    @Test
    void read() {
        var dao = injector.getInstance(ProductPositionDao.class);
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
            assertEquals(dao.read(product.getId()), product);
        }
    }

    @Test
    void all() {
        var dao = injector.getInstance(ProductPositionDao.class);
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
        var actual = dao.all();

        assertTrue(actual.size() == entries.size() && actual.containsAll(entries));
    }

    @Test
    void create() {
        var dao = injector.getInstance(ProductPositionDao.class);
        var product = new PositionsRecord(random.nextInt(), 1, 1, 19999.99, 100);
        assertNotNull(dao.create(product));
        assertEquals(product, dao.read(product.getId()));
        assertTrue(dao.delete(product));
    }

    @Test
    void update() {
        var dao = injector.getInstance(ProductPositionDao.class);
        var product = new PositionsRecord(random.nextInt(), 1, 1, 19999.99, 100);
        assertNotNull(dao.create(product));
        assertEquals(product, dao.read(product.getId()));
        product = new PositionsRecord(product.getId(), 3, 3, 39999.99, 300);
        assertTrue(dao.update(product));
        assertEquals(product, dao.read(product.getId()));
        assertTrue(dao.delete(product));
    }

    @Test
    void delete() {
        var dao = injector.getInstance(ProductPositionDao.class);
        var product = new PositionsRecord(random.nextInt(), 1, 1, 19999.99, 100);
        assertNotNull(dao.create(product));
        assertEquals(product, dao.read(product.getId()));
        assertTrue(dao.delete(product));
        assertNull(dao.read(product.getId()));
    }
}
