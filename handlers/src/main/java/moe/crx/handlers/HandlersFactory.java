package moe.crx.handlers;

import com.google.inject.Inject;
import moe.crx.servlets.ServletContextHandlerFactory;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class HandlersFactory {

    private final ResourceHandlerFactory resourceHandlerFactory;
    private final ServletContextHandlerFactory servletContextHandlerFactory;

    @Inject
    public HandlersFactory(@NotNull ResourceHandlerFactory resourceHandlerFactory,
                           @NotNull ServletContextHandlerFactory servletContextHandlerFactory) {
        this.resourceHandlerFactory = resourceHandlerFactory;
        this.servletContextHandlerFactory = servletContextHandlerFactory;
    }

    public Handler getHandlers() {
        final var list = new HandlerList();
        list.setHandlers(new Handler[] {
                resourceHandlerFactory.getHandler(),
                servletContextHandlerFactory.getHandler()
        });
        return list;
    }
}
