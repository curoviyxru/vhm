package moe.crx.factories;

import com.google.inject.Inject;
import moe.crx.Library;
import moe.crx.exceptions.InsufficientCapacityException;
import org.jetbrains.annotations.NotNull;

public class LibraryFactory {

    private final BooksFactory booksFactory;

    @Inject
    public LibraryFactory(@NotNull BooksFactory booksFactory) {
        this.booksFactory = booksFactory;
    }

    public Library library(int capacity) throws InsufficientCapacityException {
        return new Library(capacity, booksFactory);
    }
}
