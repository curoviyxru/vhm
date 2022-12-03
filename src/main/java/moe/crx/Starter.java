package moe.crx;

import com.google.inject.Guice;
import moe.crx.database.DatabaseModule;
import moe.crx.database.Reporter;
import moe.crx.jooq.tables.records.ProductsRecord;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

public final class Starter {

    public static void main(@NotNull String[] args) throws ParseException {
        final var injector = Guice.createInjector(new DatabaseModule(args));
        injector.getInstance(Flyway.class).migrate();

        final var reporter = injector.getInstance(Reporter.class);

        System.out.println("Top 10 suppliers:");
        reporter.getTopSuppliers().forEach(o -> System.out.printf("%d/%s/%s%n",
                o.getInn(), o.getName(), o.getGiro()));

        System.out.println();

        System.out.println("Suppliers by product and it's limits:");
        var limits = new HashMap<ProductsRecord, Integer>();
        limits.put(new ProductsRecord(1, null), 200);
        limits.put(new ProductsRecord(4, null), 300);
        reporter.getSuppliersByProductAndLimit(limits).forEach(o -> System.out.printf("%d/%s/%s%n",
                o.getInn(), o.getName(), o.getGiro()));

        System.out.println();

        var formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        var begin = new Date(formatter.parse("2022-01-01").getTime()).toLocalDate();
        var end = new Date(formatter.parse("2022-01-05").getTime()).toLocalDate();

        System.out.println("Products info in period:");
        var info = reporter.getProductsInfoInPeriod(begin, end);
        info.getPerDay().forEach((d, m) -> {
            System.out.printf("%s:%n", d.toString());
            m.forEach((e, s) -> System.out.printf("%s: %d items, %.2f sum%n",
                    e.getName(), s.getAmount(), s.getSum()));
            System.out.println();
        });
        info.getInPeriod().forEach((e, s) -> System.out.printf("%s: %d items, %.2f sum%n",
                e.getName(), s.getAmount(), s.getSum()));

        System.out.println();

        System.out.println("Average prices in period:");
        reporter.getAveragePriceInPeriod(begin, end).forEach((p, d) -> System.out.printf("%s: %.2f%n", p.getName(), d));

        System.out.println();

        System.out.println("Get supplied products in period:");
        reporter.getSuppliedProductsInPeriod(begin, end).forEach((o, l) -> {
            System.out.printf("%s:%n", o.getName());
            l.forEach(p -> System.out.println(p.getName()));
            if (l.isEmpty()) System.out.println("<nothing>");
            System.out.println();
        });
    }
}