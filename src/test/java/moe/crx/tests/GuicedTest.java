package moe.crx.tests;

import com.google.inject.Guice;
import com.google.inject.Injector;
import moe.crx.database.DatabaseModule;
import moe.crx.database.InputArgs;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.sql.Date;
import java.util.Locale;
import java.util.Random;

public abstract class GuicedTest {

    protected static Injector injector;
    protected static final Random random = new Random();
    protected static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    @BeforeAll
    static void init() {
        final var args = new InputArgs("localhost", "database", "postgres", "verystrongpassword");
        injector = Guice.createInjector(new DatabaseModule(args));
        injector.getInstance(Flyway.class).migrate();
    }

    protected static LocalDate getDate(String date) throws ParseException {
        return new Date(dateFormatter.parse(date).getTime()).toLocalDate();
    }

}
