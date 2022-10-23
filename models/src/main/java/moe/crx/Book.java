package moe.crx;

import lombok.Data;

@Data
public class Book {
    long id;
    BookAuthor author;
    String title;
    int year;
    long soldCopies;
}
