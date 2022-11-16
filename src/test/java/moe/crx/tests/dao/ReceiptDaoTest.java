package moe.crx.tests.dao;

import moe.crx.dao.ReceiptDao;
import moe.crx.dto.Receipt;
import moe.crx.tests.GuicedTest;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public final class ReceiptDaoTest extends GuicedTest {

    @Test
    void read() throws ParseException {
        var dao = injector.getInstance(ReceiptDao.class);
        var formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        var entries = Arrays.asList(
                new Receipt(5, new Date(formatter.parse("2022-04-06").getTime()), 333333),
                new Receipt(6, new Date(formatter.parse("2022-04-05").getTime()), 333333),
                new Receipt(7, new Date(formatter.parse("2022-01-02").getTime()), 111111),
                new Receipt(8, new Date(formatter.parse("2022-01-01").getTime()), 111111),
                new Receipt(3, new Date(formatter.parse("2022-09-02").getTime()), 555555),
                new Receipt(2, new Date(formatter.parse("2022-09-03").getTime()), 222222),
                new Receipt(1, new Date(formatter.parse("2022-01-05").getTime()), 444444),
                new Receipt(4, new Date(formatter.parse("2022-01-03").getTime()), 111111)
        );
        for (Receipt receipt : entries) {
            assertEquals(dao.read(receipt.getId()), receipt);
        }
    }

    @Test
    void all() throws ParseException {
        var dao = injector.getInstance(ReceiptDao.class);
        var formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        var entries = Arrays.asList(
                new Receipt(5, new Date(formatter.parse("2022-04-06").getTime()), 333333),
                new Receipt(6, new Date(formatter.parse("2022-04-05").getTime()), 333333),
                new Receipt(7, new Date(formatter.parse("2022-01-02").getTime()), 111111),
                new Receipt(8, new Date(formatter.parse("2022-01-01").getTime()), 111111),
                new Receipt(3, new Date(formatter.parse("2022-09-02").getTime()), 555555),
                new Receipt(2, new Date(formatter.parse("2022-09-03").getTime()), 222222),
                new Receipt(1, new Date(formatter.parse("2022-01-05").getTime()), 444444),
                new Receipt(4, new Date(formatter.parse("2022-01-03").getTime()), 111111)
        );
        assertEquals(entries, dao.all());
    }

    @Test
    void create() throws ParseException {
        var dao = injector.getInstance(ReceiptDao.class);
        var formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        var receipt = new Receipt(random.nextInt(), new Date(formatter.parse("2022-01-09").getTime()), 111111);
        assertTrue(dao.create(receipt));
        assertEquals(receipt, dao.read(receipt.getId()));
        assertTrue(dao.delete(receipt));
    }

    @Test
    void update() throws ParseException {
        var dao = injector.getInstance(ReceiptDao.class);
        var formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        var receipt = new Receipt(random.nextInt(), new Date(formatter.parse("2022-01-09").getTime()), 111111);
        assertTrue(dao.create(receipt));
        assertEquals(receipt, dao.read(receipt.getId()));
        receipt = new Receipt(receipt.getId(), new Date(formatter.parse("2022-02-09").getTime()), 222222);
        assertTrue(dao.update(receipt));
        assertEquals(receipt, dao.read(receipt.getId()));
        assertTrue(dao.delete(receipt));
    }

    @Test
    void delete() throws ParseException {
        var dao = injector.getInstance(ReceiptDao.class);
        var formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        var receipt = new Receipt(random.nextInt(), new Date(formatter.parse("2022-01-09").getTime()), 111111);
        assertTrue(dao.create(receipt));
        assertEquals(receipt, dao.read(receipt.getId()));
        assertTrue(dao.delete(receipt));
        assertNull(dao.read(receipt.getId()));
    }
}
