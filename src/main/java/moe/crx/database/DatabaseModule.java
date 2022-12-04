package moe.crx.database;

import com.google.inject.AbstractModule;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.dao.OrganizationDao;
import moe.crx.dao.ProductDao;
import moe.crx.dao.ProductPositionDao;
import moe.crx.dao.ReceiptDao;
import moe.crx.reports.Reporter;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;

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
        bind(Flyway.class).toInstance(Flyway
                .configure()
                .dataSource(String.format("jdbc:postgresql://%s/%s", args.getHostname(), args.getDatabase()),
                        args.getUsername(), args.getPassword())
                .locations("sql")
                .load());

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(String.format("jdbc:postgresql://%s/%s", args.getHostname(), args.getDatabase()));
        hikariConfig.setUsername(args.getUsername());
        hikariConfig.setPassword(args.getPassword());
        bind(HikariDataSource.class).toInstance(new HikariDataSource(hikariConfig));
    }
}
