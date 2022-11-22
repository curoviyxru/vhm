package moe.crx.database;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class HikariConnectable {

    @NotNull private final HikariDataSource dataSource;

    @Inject public HikariConnectable(@NotNull HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @NotNull protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
