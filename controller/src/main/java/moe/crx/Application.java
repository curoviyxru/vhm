package moe.crx;

import org.jetbrains.annotations.NotNull;

public class Application {
    public static void main(@NotNull String[] args) {
        System.out.println(LibraryFactory.getLibrary().filtered(String.join(" ", args)).toJson());
    }
}
