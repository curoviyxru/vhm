package moe.crx.security;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.util.security.Constraint;

import java.util.*;

public final class SecurityHandlerFactory {
    private static final String ROLE_MANAGER = "manager";
    private static final String ROLE_GUEST = "guest";

    private final ConstraintSecurityHandler security = new ConstraintSecurityHandler();

    public ConstraintSecurityHandler getHandler(LoginService loginService) {
        security.setLoginService(loginService);

        final List<ConstraintMapping> constraintMappings = new ArrayList<>();
        constraintMappings.addAll(constraintFullMapping(buildConstraint(ROLE_MANAGER),
                Arrays.asList("/products/add", "/products/delete")));
        constraintMappings.addAll(constraintGetMapping(buildConstraint(ROLE_GUEST, ROLE_MANAGER),
                Arrays.asList("/products", "/products/list", "/products/{name}/list")));

        security.setConstraintMappings(constraintMappings);
        security.setAuthenticator(new BasicAuthenticator());
        security.setDenyUncoveredHttpMethods(true);
        return security;
    }

    private static Constraint buildConstraint(String... userRoles) {
        final Constraint starterConstraint = new Constraint();
        starterConstraint.setName(Constraint.__BASIC_AUTH);
        starterConstraint.setRoles(userRoles);
        starterConstraint.setAuthenticate(true);
        return starterConstraint;
    }

    private static Collection<ConstraintMapping> constraintGetMapping(Constraint constraint,
                                                                      Collection<String> paths) {
        return constraintMapping(constraint, paths, "GET");
    }

    private static Collection<ConstraintMapping> constraintFullMapping(Constraint constraint,
                                                                       Collection<String> paths) {
        return constraintMapping(constraint, paths, "*");
    }

    private static Collection<ConstraintMapping> constraintMapping(Constraint constraint,
                                                                   Collection<String> paths,
                                                                   String method) {
        return paths.stream().map(path -> {
            final ConstraintMapping mapping = new ConstraintMapping();
            mapping.setConstraint(constraint);
            mapping.setPathSpec(path);
            mapping.setMethod(method);
            return mapping;
        }).toList();
    }
}