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
        //:( В обертывании SQLException не вижу большого смысла, т.к. close() все равно throws SQLException,
        //из-за чего в любом случае приходится добавлять catch clause. (может быть я недодумался, как и его опустить?)
        return dataSource.getConnection();
    }
}
