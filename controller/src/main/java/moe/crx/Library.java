package moe.crx;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Library {
    public HashMap<String, List<Book>> books = new HashMap<>();

    public void add(@NotNull String authorName, @NotNull String title, int year, long soldCopies) {
        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setName(authorName);

        Book book = new Book();
        book.setId(books.size());
        book.setAuthor(bookAuthor);
        book.setTitle(title);
        book.setYear(year);
        book.setSoldCopies(soldCopies);

        List<Book> authoredBooks = books.getOrDefault(authorName, new ArrayList<>());
        authoredBooks.add(book);
        books.put(authorName, authoredBooks);
    }

    public Library filtered(@NotNull String authorName) {
        Library newInstance = new Library();

        if (books.containsKey(authorName)) {
            newInstance.books.put(authorName, books.get(authorName));
        }

        return newInstance;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
