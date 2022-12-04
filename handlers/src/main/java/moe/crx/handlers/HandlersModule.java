package moe.crx.handlers;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import jakarta.ws.rs.core.Feature;
import moe.crx.api.ProductListREST;
import moe.crx.api.ProductManageREST;
import moe.crx.api.generators.ProductListContentGenerator;
import org.glassfish.jersey.jackson.JacksonFeature;

import java.util.Set;

public final class HandlersModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HandlersFactory.class);
        bind(ProductListContentGenerator.class);

        Multibinder<Feature> irestMultibinder = Multibinder.newSetBinder(binder(), Feature.class);
        irestMultibinder.addBinding().to(JacksonFeature.class);
        irestMultibinder.addBinding().to(ProductListREST.class);
        irestMultibinder.addBinding().to(ProductManageREST.class);

        Multibinder<IHandlerFactory> factoryMultibinder = Multibinder.newSetBinder(binder(), IHandlerFactory.class);
        factoryMultibinder.addBinding().to(ResourceHandlerFactory.class);
        factoryMultibinder.addBinding().to(ServletContextHandlerFactory.class);
    }

    @Provides
    public Feature[] getRests(Set<Feature> rests) {
        return rests.toArray(new Feature[0]);
    }

    @Provides
    public IHandlerFactory[] getFactories(Set<IHandlerFactory> factories) {
        return factories.toArray(new IHandlerFactory[0]);
    }
}
