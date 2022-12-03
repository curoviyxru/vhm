package moe.crx.tests;

import moe.crx.database.Reporter;
import moe.crx.jooq.tables.records.OrganizationsRecord;
import moe.crx.jooq.tables.records.ProductsRecord;
import moe.crx.reports.ProductSummary;
import moe.crx.reports.ProductsReport;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ReporterTest extends GuicedTest {

    @Test
    void getTopSuppliers() {
        var reporter = injector.getInstance(Reporter.class);
        var entries = Arrays.asList(
                new OrganizationsRecord(222222, "DNC Store", "908416510931"),
                new OrganizationsRecord(333333, "reShop Store", "234790619471"),
                new OrganizationsRecord(444444, "Online-exchange Store", "129360150954"),
                new OrganizationsRecord(111111, "N.Video Store", "614615097140"),
                new OrganizationsRecord(555555, "Random Store", "1351516316"),
                new OrganizationsRecord(1000000, "GeeksStore", "2353153141"),
                new OrganizationsRecord(1111111, "SmartBuy Store", "123146514151"),
                new OrganizationsRecord(666666, "Goldencity Store", "13413515135"),
                new OrganizationsRecord(777777, "Balislowness Store", "1353516513635"),
                new OrganizationsRecord(888888, "Noname Store", "14151356316")
        );

        var actual = reporter.getTopSuppliers();

        assertTrue(actual.size() == entries.size() && actual.containsAll(entries));
    }

    @Test
    void getSuppliersByProductAndLimit() {
        var reporter = injector.getInstance(Reporter.class);
        var suppliers =
                List.of(new OrganizationsRecord(333333, "reShop Store", "234790619471"));

        var limits = new HashMap<ProductsRecord, Integer>();
        limits.put(new ProductsRecord(1, null), 200);
        limits.put(new ProductsRecord(4, null), 300);

        assertEquals(suppliers, reporter.getSuppliersByProductAndLimit(limits));
    }

    @Test
    void getProductsInfoInPeriod() throws ParseException {
        var reporter = injector.getInstance(Reporter.class);
        var begin = getDate("2022-01-01");
        var end = getDate("2022-01-05");

        var info = new ProductsReport();
        var first = new HashMap<ProductsRecord, ProductSummary>();
        first.put(new ProductsRecord(2, "ePhone 14 Super Puper"), new ProductSummary(500, 31999500));
        var second = new HashMap<ProductsRecord, ProductSummary>();
        second.put(new ProductsRecord(4, "Gnusmas Space C22"), new ProductSummary(250, 16925000));
        var fifth = new HashMap<ProductsRecord, ProductSummary>();
        fifth.put(new ProductsRecord(3, "Ximi Reimu 11T"), new ProductSummary(1000, 19999990));
        info.getPerDay().put(getDate("2022-01-01"), first);
        info.getPerDay().put(getDate("2022-01-02"), second);
        info.getPerDay().put(getDate("2022-01-03"), new HashMap<>());
        info.getPerDay().put(getDate("2022-01-04"), new HashMap<>());
        info.getPerDay().put(getDate("2022-01-05"), fifth);
        info.getInPeriod().put(new ProductsRecord(2, "ePhone 14 Super Puper"), new ProductSummary(500, 31999500));
        info.getInPeriod().put(new ProductsRecord(3, "Ximi Reimu 11T"), new ProductSummary(1000, 19999990));
        info.getInPeriod().put(new ProductsRecord(4, "Gnusmas Space C22"), new ProductSummary(250, 16925000));

        assertEquals(info, reporter.getProductsInfoInPeriod(begin, end));
    }

    @Test
    void getAveragePriceInPeriod() throws ParseException {
        var reporter = injector.getInstance(Reporter.class);
        var begin = getDate("2022-01-01");
        var end = getDate("2022-01-05");

        var average = new HashMap<ProductsRecord, Double>();
        average.put(new ProductsRecord(2, "ePhone 14 Super Puper"), 63999.00d);
        average.put(new ProductsRecord(3, "Ximi Reimu 11T"), 19999.99d);
        average.put(new ProductsRecord(4, "Gnusmas Space C22"), 67700.00d);

        assertEquals(average, reporter.getAveragePriceInPeriod(begin, end));
    }

    @Test
    void getSuppliedProductsInPeriod() throws ParseException {
        var reporter = injector.getInstance(Reporter.class);
        var begin = getDate("2022-01-01");
        var end = getDate("2022-01-05");

        var supplied = new HashMap<OrganizationsRecord, List<ProductsRecord>>();
        supplied.put(new OrganizationsRecord(1111111, "SmartBuy Store", "123146514151"), new ArrayList<>());
        supplied.put(new OrganizationsRecord(555555, "Random Store", "1351516316"), new ArrayList<>());
        supplied.put(new OrganizationsRecord(777777, "Balislowness Store", "1353516513635"), new ArrayList<>());
        supplied.put(new OrganizationsRecord(333333, "reShop Store", "234790619471"), new ArrayList<>());
        supplied.put(new OrganizationsRecord(111111, "N.Video Store", "614615097140"), Arrays.asList(
                new ProductsRecord(4, "Gnusmas Space C22")
                , new ProductsRecord(2, "ePhone 14 Super Puper")));
        supplied.put(new OrganizationsRecord(444444, "Online-exchange Store", "129360150954"), List.of(
                new ProductsRecord(3, "Ximi Reimu 11T")));
        supplied.put(new OrganizationsRecord(999999, "FastStore", "14351516"), new ArrayList<>());
        supplied.put(new OrganizationsRecord(666666, "Goldencity Store", "13413515135"), new ArrayList<>());
        supplied.put(new OrganizationsRecord(888888, "Noname Store", "14151356316"), new ArrayList<>());
        supplied.put(new OrganizationsRecord(1000000, "GeeksStore", "2353153141"), new ArrayList<>());
        supplied.put(new OrganizationsRecord(222222, "DNC Store", "908416510931"), new ArrayList<>());

        var suppliedReverse = new HashMap<>(supplied);
        suppliedReverse.put(new OrganizationsRecord(111111, "N.Video Store", "614615097140"), Arrays.asList(
                new ProductsRecord(2, "ePhone 14 Super Puper")
                , new ProductsRecord(4, "Gnusmas Space C22")));

        var actual = reporter.getSuppliedProductsInPeriod(begin, end);

        assertTrue(actual.equals(supplied) || actual.equals(suppliedReverse));
    }
}
