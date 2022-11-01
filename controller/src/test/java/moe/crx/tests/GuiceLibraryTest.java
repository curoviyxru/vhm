package moe.crx.tests;

import com.google.inject.Guice;
import com.google.inject.Injector;
import moe.crx.InputArgs;
import moe.crx.LibraryModule;
import moe.crx.factories.BooksFactory;
import moe.crx.factories.LibraryFactory;
import org.junit.jupiter.api.BeforeEach;

import java.util.Objects;

public final class GuiceLibraryTest extends AbstractLibraryTest {

    @BeforeEach
    @Override
    public void initFactory() {
        final InputArgs args = new InputArgs(Objects.requireNonNull(this.getClass().getResource("/books.txt")).getPath(), 0);
        final Injector injector = Guice.createInjector(new LibraryModule(args));
        booksFactory = injector.getInstance(BooksFactory.class);
        libraryFactory = injector.getInstance(LibraryFactory.class);
    }
}
