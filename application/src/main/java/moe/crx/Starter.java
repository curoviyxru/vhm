package moe.crx;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.jetbrains.annotations.NotNull;

public final class Starter {
    public static void main(@NotNull String[] args) {
        final Injector injector = Guice.createInjector(new LoggerModule(args));
        injector.getInstance(Application.class).waitForInput();
    }
}
