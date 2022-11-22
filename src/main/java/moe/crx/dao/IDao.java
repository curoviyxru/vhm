package moe.crx.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IDao<T> {

    @Nullable T read(int id);
    @NotNull List<@NotNull T> all();
    boolean create(@NotNull T item);
    boolean update(@NotNull T item);
    boolean delete(@NotNull T item);
}
