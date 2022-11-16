package moe.crx.dao;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.util.List;

public abstract class AbstractDao<T> {
    @Getter @NotNull private final Connection connection;

    public AbstractDao(@NotNull Connection connection) {
        this.connection = connection;
    }

    abstract @Nullable T read(int id);
    abstract @NotNull List<@NotNull T> all();
    abstract boolean create(@NotNull T item);
    abstract boolean update(@NotNull T item);
    abstract boolean delete(@NotNull T item);
}
