package moe.crx;

import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;

public final class Starter {

    public static void main(@NotNull String[] args) {
        final InputArgs inputArgs = new InputArgs(args);
        final Flyway flyway = Flyway
                .configure()
                .dataSource(String.format("jdbc:postgresql://%s/%s", inputArgs.getHostname(), inputArgs.getDatabase()),
                        inputArgs.getUsername(), inputArgs.getPassword())
                .locations("sql")
                .load();

        flyway.migrate();
        System.out.println("Migrations applied successfully");
    }
}