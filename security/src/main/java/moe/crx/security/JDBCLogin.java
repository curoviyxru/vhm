package moe.crx.security;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import lombok.Getter;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.server.Server;
import org.jetbrains.annotations.NotNull;

public final class JDBCLogin {

    private final JDBCLoginService loginService;

    @Getter private final ConstraintSecurityHandler securityHandler;

    @Inject public JDBCLogin(@Named("jdbcConfigPath") @NotNull String jdbcConfigPath,
                             SecurityHandlerFactory securityHandlerFactory) {
        this.loginService = new JDBCLoginService("login", jdbcConfigPath);
        this.securityHandler = securityHandlerFactory.getHandler(loginService);
    }

    public void applyTo(@NotNull Server server) {
        server.addBean(loginService);
        server.setHandler(securityHandler);
    }
}
