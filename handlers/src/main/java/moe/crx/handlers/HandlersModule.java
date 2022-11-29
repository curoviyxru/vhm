package moe.crx.handlers;

import com.google.inject.AbstractModule;

public final class HandlersModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ResourceHandlerFactory.class);
        bind(HandlersFactory.class);
    }
}
