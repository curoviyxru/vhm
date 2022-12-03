package moe.crx.servlets;

import com.google.inject.AbstractModule;

public final class ServletsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProductListContentGenerator.class);
        bind(ProductsAddServlet.class);
        bind(ProductsListServlet.class);
        bind(ServletContextHandlerFactory.class);
    }
}
