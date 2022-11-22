package moe.crx.tests;

import moe.crx.database.Reporter;
import moe.crx.dto.Organization;
import moe.crx.dto.Product;
import moe.crx.dto.reports.ProductSummary;
import moe.crx.dto.reports.ProductsReport;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ReporterTest extends GuicedTest {

    @Test
    void getTopSuppliers() {
        var reporter = injector.getInstance(Reporter.class);
        var suppliers = Arrays.asList(
                new Organization(222222, "DNC Store", "908416510931"),
                new Organization(333333, "reShop Store", "234790619471"),
                new Organization(444444, "Online-exchange Store", "129360150954"),
                new Organization(111111, "N.Video Store", "614615097140"),
                new Organization(555555, "Random Store", "1351516316"),
                new Organization(1000000, "GeeksStore", "2353153141"),
                new Organization(1111111, "SmartBuy Store", "123146514151"),
                new Organization(666666, "Goldencity Store", "13413515135"),
                new Organization(777777, "Balislowness Store", "1353516513635"),
                new Organization(888888, "Noname Store", "14151356316")
        );

        assertEquals(suppliers, reporter.getTopSuppliers());
    }

    @Test
    void getSuppliersByProductAndLimit() {
        var reporter = injector.getInstance(Reporter.class);
        var suppliers = List.of(new Organization(333333, "reShop Store", "234790619471"));

        var limits = new HashMap<Product, Integer>();
        limits.put(new Product(1, null), 200);
        limits.put(new Product(4, null), 300);

        assertEquals(suppliers, reporter.getSuppliersByProductAndLimit(limits));
    }

    @Test
    void getProductsInfoInPeriod() throws ParseException {
        var reporter = injector.getInstance(Reporter.class);
        var formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        var begin = new Date(formatter.parse("2022-01-01").getTime());
        var end = new Date(formatter.parse("2022-01-05").getTime());

        var info = new ProductsReport();
        var first = new HashMap<Product, ProductSummary>();
        first.put(new Product(2, "ePhone 14 Super Puper"), new ProductSummary(500, 31999500));
        var second = new HashMap<Product, ProductSummary>();
        second.put(new Product(4, "Gnusmas Space C22"), new ProductSummary(250, 16925000));
        var fifth = new HashMap<Product, ProductSummary>();
        fifth.put(new Product(3, "Ximi Reimu 11T"), new ProductSummary(1000, 19999000));
        info.getPerDay().put(new Date(formatter.parse("2022-01-01").getTime()), first);
        info.getPerDay().put(new Date(formatter.parse("2022-01-02").getTime()), second);
        info.getPerDay().put(new Date(formatter.parse("2022-01-03").getTime()), new HashMap<>());
        info.getPerDay().put(new Date(formatter.parse("2022-01-04").getTime()), new HashMap<>());
        info.getPerDay().put(new Date(formatter.parse("2022-01-05").getTime()), fifth);
        info.getInPeriod().put(new Product(2, "ePhone 14 Super Puper"), new ProductSummary(500, 31999500));
        info.getInPeriod().put(new Product(3, "Ximi Reimu 11T"), new ProductSummary(1000, 19999000));
        info.getInPeriod().put(new Product(4, "Gnusmas Space C22"), new ProductSummary(250, 16925000));

        assertEquals(info, reporter.getProductsInfoInPeriod(begin, end));
    }

    @Test
    void getAveragePriceInPeriod() throws ParseException {
        var reporter = injector.getInstance(Reporter.class);
        var formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        var begin = new Date(formatter.parse("2022-01-01").getTime());
        var end = new Date(formatter.parse("2022-01-05").getTime());

        var average = new HashMap<Product, Double>();
        average.put(new Product(2, "ePhone 14 Super Puper"), 63999.00d);
        average.put(new Product(3, "Ximi Reimu 11T"), 19999.99d);
        average.put(new Product(4, "Gnusmas Space C22"), 67700.00d);

        assertEquals(average, reporter.getAveragePriceInPeriod(begin, end));
    }

    @Test
    void getSuppliedProductsInPeriod() throws ParseException {
        var reporter = injector.getInstance(Reporter.class);
        var formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        var begin = new Date(formatter.parse("2022-01-01").getTime());
        var end = new Date(formatter.parse("2022-01-05").getTime());

        var supplied = new HashMap<Organization, List<Product>>();
        supplied.put(new Organization(1111111, "SmartBuy Store", "123146514151"), new ArrayList<>());
        supplied.put(new Organization(555555, "Random Store", "1351516316"), new ArrayList<>());
        supplied.put(new Organization(777777, "Balislowness Store", "1353516513635"), new ArrayList<>());
        supplied.put(new Organization(333333, "reShop Store", "234790619471"), new ArrayList<>());
        supplied.put(new Organization(111111, "N.Video Store", "614615097140"), Arrays.asList(new Product(4, "Gnusmas Space C22"), new Product(2, "ePhone 14 Super Puper")));
        supplied.put(new Organization(444444, "Online-exchange Store", "129360150954"), List.of(new Product(3, "Ximi Reimu 11T")));
        supplied.put(new Organization(999999, "FastStore", "14351516"), new ArrayList<>());
        supplied.put(new Organization(666666, "Goldencity Store", "13413515135"), new ArrayList<>());
        supplied.put(new Organization(888888, "Noname Store", "14151356316"), new ArrayList<>());
        supplied.put(new Organization(1000000, "GeeksStore", "2353153141"), new ArrayList<>());
        supplied.put(new Organization(222222, "DNC Store", "908416510931"), new ArrayList<>());

        assertEquals(supplied, reporter.getSuppliedProductsInPeriod(begin, end));
    }
}
