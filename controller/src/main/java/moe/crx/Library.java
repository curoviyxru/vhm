package moe.crx;

import moe.crx.exceptions.EmptyCellException;
import moe.crx.exceptions.InsufficientCapacityException;
import moe.crx.factories.BooksFactory;
import moe.crx.models.Book;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public final class Library {
    private final Book[] cells;

    public Library(int capacity, @NotNull BooksFactory factory) {
        cells = new Book[capacity];

        Collection<Book> books = factory.books();
        if (books.size() > cells.length)
            throw new InsufficientCapacityException(String.format("Cannot fit %d books in library with capacity %d.", books.size(), cells.length));

        books.toArray(cells);
    }

    private int firstFreeIndex() {
        for (int i = 0; i < cells.length; ++i)
            if (cells[i] == null) return i;

        return -1;
    }

    public void add(@NotNull Book book) {
        int index = firstFreeIndex();
        if (index == -1)
            throw new InsufficientCapacityException(String.format("Cannot add another book in full library. (%d cells occupied)", cells.length));

        cells[index] = book;
    }

    @NotNull
    public Book get(int index) {
        Book book = cells[index];

        if (book != null)
            System.out.printf("[GET] %d: %s by %s%n", index, book.getName(), book.getAuthor().getName());
        else
            throw new EmptyCellException(String.format("Cell %d is empty.", index));

        cells[index] = null;
        return book;
    }

    public void printContents() {
        System.out.printf("Library #%d contents:%n", hashCode());
        for (int i = 0; i < cells.length; ++i) {
            Book book = cells[i];

            if (book != null)
                System.out.printf("%d: %s by %s%n", i, book.getName(), book.getAuthor().getName());
            else
                System.out.printf("%d: empty cell / null%n", i);
        }
    }
}
