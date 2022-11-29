package moe.crx.security;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import java.util.Objects;

public final class SecurityModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("jdbcConfigPath"))
                .toInstance(Objects.requireNonNull(getClass().getResource("/jdbc_config")).toExternalForm());
        bind(JDBCLogin.class);
        bind(SecurityHandlerFactory.class);
    }
}
