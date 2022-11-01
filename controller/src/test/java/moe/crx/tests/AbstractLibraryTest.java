package moe.crx.tests;

import moe.crx.Library;
import moe.crx.exceptions.EmptyCellException;
import moe.crx.exceptions.InsufficientCapacityException;
import moe.crx.factories.BooksFactory;
import moe.crx.factories.LibraryFactory;
import moe.crx.models.Book;
import moe.crx.models.BookAuthor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AbstractLibraryTest {

    protected LibraryFactory libraryFactory;
    protected BooksFactory booksFactory;
    private final int CAPACITY_GAP = 100;

    /**
     * Метод для инициализации тестовых фабрик.
     */
    @BeforeEach
    public abstract void initFactory();

    /**
     * Библиотека бросает исключение при создании, если ее вместимость меньше чем количество книг, возвращаемое фабрикой.
     */
    @Test
    public void testCapacityExceptionWhenConstructing() {
        assertThrows(InsufficientCapacityException.class, () -> libraryFactory.library(1));
        assertThrows(InsufficientCapacityException.class, () -> libraryFactory.library(10));
        assertThrows(InsufficientCapacityException.class, () -> libraryFactory.library(booksFactory.books().size() - 1));
    }

    /**
     * При создании библиотеки все книги расставлены по ячейкам в порядке как они возвращаются фабрикой книг. Остальные ячейки пусты.
     */
    @Test
    public void testCellsOrdering() {
        final Book[] books = booksFactory.books().toArray(new Book[0]);
        final Library library = libraryFactory.library(books.length + CAPACITY_GAP);

        for (int i = 0; i < books.length; ++i) {
            assertEquals(books[i], library.get(i));
        }
        for (int i = books.length; i < books.length + CAPACITY_GAP; ++i) {
            int finalI = i;
            assertThrows(EmptyCellException.class, () -> library.get(finalI));
        }
    }

    /**
     * При взятии книги информация о ней и ячейке выводится.
     */
    @Test
    public void testPrintingWhenGetting() {
        final Library library = libraryFactory.library(booksFactory.books().size());
        final PrintStream stdOut = System.out;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream printStream = new PrintStream(baos)) {
            System.setOut(printStream);

            for (int i = 0; i < booksFactory.books().size(); ++i) {
                baos.reset();
                Book book = library.get(i);
                assertEquals(String.format("[GET] %d: %s by %s%n", i, book.getName(), book.getAuthor().getName()), baos.toString());
            }
        } catch (IOException e) {
            System.setOut(stdOut);
            throw new RuntimeException(e);
        }

        System.setOut(stdOut);
    }

    /**
     * При попытке взять книгу из пустой ячейки библиотека бросает исключение.
     */
    @Test
    public void testEmptyCellExceptionWhenGetting() {
        final Library library = libraryFactory.library(booksFactory.books().size() + CAPACITY_GAP);

        for (int i = booksFactory.books().size(); i < booksFactory.books().size() + CAPACITY_GAP; ++i) {
            int finalI = i;
            assertThrows(EmptyCellException.class, () -> library.get(finalI));
        }
        for (int i = 10; i < 20; ++i) {
            library.get(i);
            int finalI = i;
            assertThrows(EmptyCellException.class, () -> library.get(finalI));
        }
    }

    /**
     * При взятии книги возвращается именно та книга, что была в этой ячейке.
     */
    @Test
    public void testBooksGetting() {
        final Book[] books = booksFactory.books().toArray(new Book[0]);
        final Library library = libraryFactory.library(books.length);

        for (int i = 0; i < books.length; ++i) {
            assertEquals(books[i], library.get(i));
        }
    }

    /**
     * При добавлении книги она размещается в первой свободной ячейке.
     */
    @Test
    public void testBookAdding() {
        final Book[] books = booksFactory.books().toArray(new Book[0]);
        final Library library = libraryFactory.library(books.length + CAPACITY_GAP);

        for (int i = 10; i < 20; ++i)
            library.get(i);
        for (int i = 0; i < 20; ++i)
            library.add(books[i]);
        for (int i = 10; i < 20; ++i)
            assertEquals(books[i - 10], library.get(i));
        for (int i = books.length; i < books.length + 10; ++i)
            assertEquals(books[i - books.length + 10], library.get(i));
    }

    /**
     * Если при добавлении книги свободных ячеек нет, библиотека бросает исключение.
     */
    @Test
    public void testCapacityExceptionWhenAdding() {
        final Book testBook = new Book("Test Book", new BookAuthor("Test Author"));
        assertThrows(InsufficientCapacityException.class, () -> libraryFactory.library(booksFactory.books().size()).add(testBook));
    }

    /**
     * Вызов метода “напечатать в консоль содержимое” выводит информацию о содержимом ячеек библиотеки.
     */
    @Test
    public void testPrintingContents() {
        final Library library = libraryFactory.library(booksFactory.books().size() + CAPACITY_GAP);
        final PrintStream stdOut = System.out;
        final StringBuilder builder = new StringBuilder();
        String printed;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream printStream = new PrintStream(baos)) {
            System.setOut(printStream);

            library.printContents();
            printed = baos.toString();
        } catch (IOException e) {
            System.setOut(stdOut);
            throw new RuntimeException(e);
        }

        builder.append(String.format("Library #%d contents:%n", library.hashCode()));
        for (int i = 0; i < booksFactory.books().size(); ++i) {
            Book book = library.get(i);
            builder.append(String.format("%d: %s by %s%n", i, book.getName(), book.getAuthor().getName()));
        }
        for (int i = booksFactory.books().size(); i < booksFactory.books().size() + CAPACITY_GAP; ++i) {
            builder.append(String.format("%d: empty cell / null%n", i));
        }

        assertEquals(builder.toString(), printed);
        System.setOut(stdOut);
    }
}
