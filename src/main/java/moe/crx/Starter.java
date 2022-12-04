package moe.crx;

import com.google.inject.Guice;
import moe.crx.core.CoreModule;
import moe.crx.core.InputArgs;
import moe.crx.core.ServerFactory;
import moe.crx.database.DatabaseModule;
import moe.crx.handlers.HandlersFactory;
import moe.crx.handlers.HandlersModule;
import moe.crx.security.JDBCLogin;
import moe.crx.security.SecurityModule;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;

public final class Starter {

    public static void main(@NotNull String[] rawArgs) throws Exception {
        var injector = Guice.createInjector(new CoreModule(rawArgs));
        final var args = injector.getInstance(InputArgs.class);
        injector = injector.createChildInjector(
                new DatabaseModule(args),
                new SecurityModule(),
                new HandlersModule()
        );

        injector.getInstance(Flyway.class).migrate();

        final var server = injector.getInstance(ServerFactory.class).getServer(8080);
        final var login = injector.getInstance(JDBCLogin.class);
        final var factory = injector.getInstance(HandlersFactory.class);
        login.getSecurityHandler().setHandler(factory.getHandlers());
        login.applyTo(server);
        server.start();
    }
}