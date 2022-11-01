package moe.crx;

import com.google.inject.AbstractModule;
import moe.crx.factories.BooksFactory;
import moe.crx.factories.FileBookFactory;
import org.jetbrains.annotations.NotNull;

public final class LibraryModule extends AbstractModule {

    private final InputArgs args;

    public LibraryModule(@NotNull InputArgs args) {
        this.args = args;
    }

    public LibraryModule(@NotNull String[] args) {
        this(new InputArgs(args));
    }

    @Override
    protected void configure() {
        bind(InputArgs.class).toInstance(args);
        bind(BooksFactory.class).toInstance(new FileBookFactory(args.getFilePath()));
    }
}
