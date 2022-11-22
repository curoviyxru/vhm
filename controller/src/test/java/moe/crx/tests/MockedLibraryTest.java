package moe.crx.tests;

import moe.crx.Library;
import moe.crx.factories.BooksFactory;
import moe.crx.factories.LibraryFactory;
import moe.crx.models.Book;
import moe.crx.models.BookAuthor;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockedLibraryTest extends AbstractLibraryTest {

    @BeforeEach
    @Override
    public void initFactory() {
        booksFactory = mock(BooksFactory.class);
        when(booksFactory.books()).thenReturn(List.of(
                new Book("Mothering Heights", new BookAuthor("Dav Pilkey")),
                new Book("American Marxism", new BookAuthor("Mark R. Levin")),
                new Book("Big Shot", new BookAuthor("Jeff Kinney")),
                new Book("The Boy, The Mole, The Fox and the Horse", new BookAuthor("Charlie Macksey")),
                new Book("Atomic Habits", new BookAuthor("James Clear")),
                new Book("It Ends With Us", new BookAuthor("Colleen Hoover")),
                new Book("The Very Hungry Caterpillar", new BookAuthor("Eric Carle")),
                new Book("Oh, the Places You'll Go!", new BookAuthor("Dr. Seuss")),
                new Book("The Four Agreements", new BookAuthor("Don Miguel Ruiz")),
                new Book("The Four Winds", new BookAuthor("Kristen Hannah")),
                new Book("They Both Die at the End", new BookAuthor("Adam Silvera")),
                new Book("The Song of Achilles", new BookAuthor("Madeline Miller")),
                new Book("The Midnight Library", new BookAuthor("Matt Haig")),
                new Book("Where the Crawdads Sing", new BookAuthor("Delia Owens")),
                new Book("Cat Kid Comic Club", new BookAuthor("Dav Pilkey")),
                new Book("I Love You to the Moon and Back", new BookAuthor("Hepworth/Warnes")),
                new Book("The Body Keeps the Score", new BookAuthor("Bessel van der Kolk")),
                new Book("Rowley Jefferson's Awesome Friendly Spooky Stories", new BookAuthor("Jeff Kinney")),
                new Book("The Seven Husbands of Evelyn Hugo", new BookAuthor("Taylor Jenkins Reid")),
                new Book("Grime and Punishment", new BookAuthor("Dav Pilkey")),
                new Book("The Hill We Climb", new BookAuthor("Amanda Gorman")),
                new Book("Brown Bear, Brown Bear, What Do You See?", new BookAuthor("Bill Martin Jr.")),
                new Book("The Deep End", new BookAuthor("Jeff Kinney")),
                new Book("The Last Thing He Told Me", new BookAuthor("Laura Dave")),
                new Book("The Judge's List", new BookAuthor("John Grisham"))
        ));

        libraryFactory = mock(LibraryFactory.class);
        when(libraryFactory.library(anyInt())).thenAnswer(i -> new Library(i.getArgument(0), booksFactory));
    }
}
