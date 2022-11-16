package moe.crx.database;

import com.google.inject.AbstractModule;
import moe.crx.dao.OrganizationDao;
import moe.crx.dao.ProductDao;
import moe.crx.dao.ProductPositionDao;
import moe.crx.dao.ReceiptDao;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseModule extends AbstractModule {

    @NotNull private final InputArgs args;

    public DatabaseModule(@NotNull String[] args) {
        this(new InputArgs(args));
    }

    public DatabaseModule(@NotNull InputArgs args) {
        this.args = args;
    }

    @Override
    protected void configure() {
        bind(OrganizationDao.class);
        bind(ProductDao.class);
        bind(ProductPositionDao.class);
        bind(ReceiptDao.class);
        bind(Reporter.class);
        bind(InputArgs.class).toInstance(args);
        try {
            bind(Connection.class).toInstance(DriverManager.getConnection(String.format("jdbc:postgresql://%s/%s", args.getHostname(), args.getDatabase()),
                    args.getUsername(), args.getPassword()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        bind(Flyway.class).toInstance(Flyway
                .configure()
                .dataSource(String.format("jdbc:postgresql://%s/%s", args.getHostname(), args.getDatabase()),
                        args.getUsername(), args.getPassword())
                .locations("sql")
                .load());
    }
}
