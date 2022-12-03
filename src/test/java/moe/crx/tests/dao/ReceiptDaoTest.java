package moe.crx.tests.dao;

import moe.crx.dao.ReceiptDao;
import moe.crx.jooq.tables.records.ReceiptsRecord;
import moe.crx.tests.GuiceTest;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public final class ReceiptDaoTest extends GuiceTest<ReceiptDao> {

    public ReceiptDaoTest() {
        super(ReceiptDao.class);
    }

    @Test
    void read() throws ParseException {
        var entries = Arrays.asList(
                new ReceiptsRecord(5, getDate("2022-04-06"), 333333),
                new ReceiptsRecord(6, getDate("2022-04-05"), 333333),
                new ReceiptsRecord(7, getDate("2022-01-02"), 111111),
                new ReceiptsRecord(8, getDate("2022-01-01"), 111111),
                new ReceiptsRecord(3, getDate("2022-09-02"), 555555),
                new ReceiptsRecord(2, getDate("2022-09-03"), 222222),
                new ReceiptsRecord(1, getDate("2022-01-05"), 444444),
                new ReceiptsRecord(4, getDate("2022-01-03"), 111111)
        );
        for (ReceiptsRecord receipt : entries) {
            assertEquals(instance.read(receipt.getId()), receipt);
        }
    }

    @Test
    void all() throws ParseException {
        var entries = Arrays.asList(
                new ReceiptsRecord(5, getDate("2022-04-06"), 333333),
                new ReceiptsRecord(6, getDate("2022-04-05"), 333333),
                new ReceiptsRecord(7, getDate("2022-01-02"), 111111),
                new ReceiptsRecord(8, getDate("2022-01-01"), 111111),
                new ReceiptsRecord(3, getDate("2022-09-02"), 555555),
                new ReceiptsRecord(2, getDate("2022-09-03"), 222222),
                new ReceiptsRecord(1, getDate("2022-01-05"), 444444),
                new ReceiptsRecord(4, getDate("2022-01-03"), 111111)
        );
        var actual = instance.all();

        assertTrue(actual.size() == entries.size() && actual.containsAll(entries));
    }

    @Test
    void create() throws ParseException {
        var receipt = new ReceiptsRecord(random.nextInt(), getDate("2022-01-09"), 111111);
        assertNotNull(instance.create(receipt));
        assertEquals(receipt, instance.read(receipt.getId()));
        assertTrue(instance.delete(receipt));
    }

    @Test
    void update() throws ParseException {
        var receipt = new ReceiptsRecord(random.nextInt(), getDate("2022-01-09"), 111111);
        assertNotNull(instance.create(receipt));
        assertEquals(receipt, instance.read(receipt.getId()));
        receipt = new ReceiptsRecord(receipt.getId(), getDate("2022-02-09"), 222222);
        assertTrue(instance.update(receipt));
        assertEquals(receipt, instance.read(receipt.getId()));
        assertTrue(instance.delete(receipt));
    }

    @Test
    void delete() throws ParseException {
        var receipt = new ReceiptsRecord(random.nextInt(), getDate("2022-01-09"), 111111);
        assertNotNull(instance.create(receipt));
        assertEquals(receipt, instance.read(receipt.getId()));
        assertTrue(instance.delete(receipt));
        assertNull(instance.read(receipt.getId()));
    }
}
