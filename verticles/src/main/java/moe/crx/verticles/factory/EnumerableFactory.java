package moe.crx.verticles.factory;

import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.impl.JavaVerticleFactory;

import java.util.concurrent.Callable;

public abstract class EnumerableFactory<T extends Verticle> extends JavaVerticleFactory {

    private int nextId;

    public EnumerableFactory(int nextId) {
        this.nextId = nextId;
    }

    public EnumerableFactory() {
        this(0);
    }

    public int getNextId() {
        return nextId++;
    }

    public String deployName() {
        return prefix() + ':' + verticleType().getName();
    }

    @Override
    public String prefix() {
        return verticleType().getSimpleName() + "Factory";
    }

    @Override
    public void createVerticle(String verticleName, ClassLoader classLoader, Promise<Callable<Verticle>> promise) {
        promise.complete(this::createVerticle);
    }

    public abstract Class<T> verticleType();

    public abstract T createVerticle();
}
