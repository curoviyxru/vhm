package moe.crx.servlets;

import com.google.inject.Inject;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jetbrains.annotations.NotNull;

public final class ServletContextHandlerFactory {

    private final ServletContextHandler handler;

    @Inject public ServletContextHandlerFactory(@NotNull ProductsListServlet productsListServlet,
                                                @NotNull ProductsAddServlet productsAddServlet) {
        handler = new ServletContextHandler();
        handler.setContextPath("/");
        handler.addServlet(new ServletHolder(productsListServlet), productsListServlet.getServletPath());
        handler.addServlet(new ServletHolder(productsAddServlet), productsAddServlet.getServletPath());
    }

    @NotNull public ServletContextHandler getHandler() {
        return handler;
    }
}
