package moe.crx;

import lombok.Data;

@Data
public class Book {
    public long id;
    public BookAuthor author;
    public String title;
    public int year;
    public long soldCopies;
}
