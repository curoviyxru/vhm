package moe.crx.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class Book {
    private String name;
    private BookAuthor author;
}
