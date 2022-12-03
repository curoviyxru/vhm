package moe.crx.dao;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.database.HikariConnectable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.*;

import java.util.List;

public abstract class AbstractDao<Type extends UpdatableRecord<?>, KeyType> extends HikariConnectable {

    private final Table<Type> table;
    private final TableField<Type, KeyType> keyField;
    private final boolean isKeySerial;
    private final TableField<Type, ?>[] exclusiveFields;

    @Inject public AbstractDao(@NotNull HikariDataSource dataSource,
                               @NotNull Table<Type> table,
                               @NotNull TableField<Type, KeyType> keyField,
                               boolean isKeySerial,
                               @NotNull TableField<Type, ?> ... exclusiveFields) {
        super(dataSource);
        this.table = table;
        this.keyField = keyField;
        this.isKeySerial = isKeySerial;
        this.exclusiveFields = exclusiveFields;
    }

    public @Nullable Type read(KeyType id) {
        try (var c = getConnection()) {
            return c.context().fetchOne(table, keyField.eq(id));
        }
    }

    public @NotNull List<@NotNull Type> all() {
        try (var c = getConnection()) {
            return c.context().fetch(table);
        }
    }

    public @Nullable Type create(@NotNull Type item) {
        try (var c = getConnection()) {
            if (isKeySerial)
                item.reset(keyField);
            return c.context()
                    .insertInto(table)
                    .set(item)
                    .onConflict(exclusiveFields)
                    .doUpdate()
                    .set(item)
                    .returning()
                    .fetchOne();
        }
    }

    public boolean update(@NotNull Type item) {
        try (var c = getConnection()) {
            return c.context().executeUpdate(item) != 0;
        }
    }

    public boolean delete(@NotNull Type item) {
        try (var c = getConnection()) {
            return c.context().executeDelete(item) != 0;
        }
    }
}
