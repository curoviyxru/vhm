package moe.crx;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Library {
    public List<Book> books = new ArrayList<>();

    public void add(@NotNull String authorName, @NotNull String title, int year, long soldCopies) {
        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setName(authorName);

        Book book = new Book();
        book.setId(books.size());
        book.setAuthor(bookAuthor);
        book.setTitle(title);
        book.setYear(year);
        book.setSoldCopies(soldCopies);

        books.add(book);
    }

    public Library filtered(@NotNull String authorName) {
        Library newInstance = new Library();
        newInstance.books = books.stream()
                .filter(book -> book.getAuthor().getName().equals(authorName))
                .toList();

        return newInstance;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
