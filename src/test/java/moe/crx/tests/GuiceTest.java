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

public abstract class GuiceTest<T> {

    private static Injector injector;
    protected T instance;
    protected final Random random = new Random();
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public GuiceTest(Class<T> clazz) {
        instance = injector.getInstance(clazz);
    }

    protected LocalDate getDate(String date) throws ParseException {
        return new Date(dateFormatter.parse(date).getTime()).toLocalDate();
    }

    @BeforeAll
    static void init() {
        injector = Guice.createInjector(new DatabaseModule(new InputArgs(
                "localhost", "database", "postgres", "verystrongpassword")));
        injector.getInstance(Flyway.class).migrate();
    }

}
