package moe.crx.handlers;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

public final class ResourceHandlerFactory {

    public Handler getHandler() {
        var contextHandler = new ContextHandler("/");
        var resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setBaseResource(Resource.newResource(getClass().getResource("/static")));
        resourceHandler.setWelcomeFiles(new String[]{ "help.html" });
        contextHandler.setHandler(resourceHandler);
        return contextHandler;
    }
}
