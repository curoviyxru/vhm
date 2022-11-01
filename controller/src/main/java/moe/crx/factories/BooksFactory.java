package moe.crx.factories;

import moe.crx.models.Book;

import java.util.Collection;

public interface BooksFactory {
    Collection<Book> books();
}
