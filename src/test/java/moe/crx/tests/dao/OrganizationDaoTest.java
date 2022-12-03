package moe.crx.tests.dao;

import moe.crx.dao.OrganizationDao;
import moe.crx.jooq.tables.records.OrganizationsRecord;
import moe.crx.tests.GuicedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public final class OrganizationDaoTest extends GuicedTest {

    @Test
    void read() {
        var dao = injector.getInstance(OrganizationDao.class);
        var entries = Arrays.asList(
                new OrganizationsRecord(111111, "N.Video Store", "614615097140"),
                new OrganizationsRecord(222222, "DNC Store", "908416510931"),
                new OrganizationsRecord(333333, "reShop Store", "234790619471"),
                new OrganizationsRecord(444444, "Online-exchange Store", "129360150954"),
                new OrganizationsRecord(555555, "Random Store", "1351516316"),
                new OrganizationsRecord(666666, "Goldencity Store", "13413515135"),
                new OrganizationsRecord(777777, "Balislowness Store", "1353516513635"),
                new OrganizationsRecord(888888, "Noname Store", "14151356316"),
                new OrganizationsRecord(999999, "FastStore", "14351516"),
                new OrganizationsRecord(1000000, "GeeksStore", "2353153141"),
                new OrganizationsRecord(1111111, "SmartBuy Store", "123146514151")
        );
        for (OrganizationsRecord org : entries) {
            assertEquals(dao.read(org.getInn()), org);
        }
    }

    @Test
    void all() {
        var dao = injector.getInstance(OrganizationDao.class);
        var entries = Arrays.asList(
                new OrganizationsRecord(111111, "N.Video Store", "614615097140"),
                new OrganizationsRecord(222222, "DNC Store", "908416510931"),
                new OrganizationsRecord(333333, "reShop Store", "234790619471"),
                new OrganizationsRecord(444444, "Online-exchange Store", "129360150954"),
                new OrganizationsRecord(555555, "Random Store", "1351516316"),
                new OrganizationsRecord(666666, "Goldencity Store", "13413515135"),
                new OrganizationsRecord(777777, "Balislowness Store", "1353516513635"),
                new OrganizationsRecord(888888, "Noname Store", "14151356316"),
                new OrganizationsRecord(999999, "FastStore", "14351516"),
                new OrganizationsRecord(1000000, "GeeksStore", "2353153141"),
                new OrganizationsRecord(1111111, "SmartBuy Store", "123146514151")
        );
        var actual = dao.all();

        assertTrue(actual.size() == entries.size() && actual.containsAll(entries));
    }

    @Test
    void create() {
        var dao = injector.getInstance(OrganizationDao.class);
        var org = new OrganizationsRecord(random.nextInt(), "UnitTest Store", String.valueOf(random.nextLong()));
        assertNotNull(dao.create(org));
        assertEquals(org, dao.read(org.getInn()));
        assertTrue(dao.delete(org));
    }

    @Test
    void update() {
        var dao = injector.getInstance(OrganizationDao.class);
        var org = new OrganizationsRecord(random.nextInt(), "UnitTest Store", String.valueOf(random.nextLong()));
        assertNotNull(dao.create(org));
        assertEquals(org, dao.read(org.getInn()));
        org = new OrganizationsRecord(org.getInn(), "UnitTest Store 2", String.valueOf(random.nextLong()));
        assertTrue(dao.update(org));
        assertEquals(org, dao.read(org.getInn()));
        assertTrue(dao.delete(org));
    }

    @Test
    void delete() {
        var dao = injector.getInstance(OrganizationDao.class);
        var org = new OrganizationsRecord(random.nextInt(), "UnitTest Store", String.valueOf(random.nextLong()));
        assertNotNull(dao.create(org));
        assertEquals(org, dao.read(org.getInn()));
        assertTrue(dao.delete(org));
        assertNull(dao.read(org.getInn()));
    }
}
