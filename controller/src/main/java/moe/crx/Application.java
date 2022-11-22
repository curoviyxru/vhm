package moe.crx;

import com.google.inject.Guice;
import com.google.inject.Injector;
import moe.crx.exceptions.InsufficientCapacityException;
import moe.crx.factories.LibraryFactory;
import org.jetbrains.annotations.NotNull;

public final class Application {

    public static void main(@NotNull String[] args) throws InsufficientCapacityException {
        final Injector injector = Guice.createInjector(new LibraryModule(args));
        final Library library = injector.getInstance(LibraryFactory.class).library(injector.getInstance(InputArgs.class).getCapacity());
        library.printContents();
    }
}
