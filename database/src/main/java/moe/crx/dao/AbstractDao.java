package moe.crx.dao;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.database.HikariConnectable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDao<Type extends UpdatableRecord<?>> extends HikariConnectable {

    private final Table<Type> table;
    private final TableField<Type, Integer> keyField;
    private final TableField<Type, ?>[] exclusiveFields;

    @Inject
    public AbstractDao(@NotNull HikariDataSource dataSource,
                       @NotNull Table<Type> table,
                       @NotNull TableField<Type, Integer> keyField,
                       @NotNull TableField<Type, ?> ... exclusiveFields) {
        super(dataSource);
        this.table = table;
        this.keyField = keyField;
        this.exclusiveFields = exclusiveFields;
    }

    public @Nullable Type read(int id) {
        try (var connection = getConnection()) {
            return DSL.using(connection, SQLDialect.POSTGRES)
                    .fetchOne(table, keyField.eq(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public @NotNull List<@NotNull Type> all() {
        try (var connection = getConnection()) {
            return DSL.using(connection, SQLDialect.POSTGRES)
                    .fetch(table);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public @Nullable Type create(@NotNull Type item) {
        try (var connection = getConnection()) {
            item.reset(keyField);
            return DSL.using(connection, SQLDialect.POSTGRES)
                    .insertInto(table)
                    .set(item)
                    .onConflict(exclusiveFields)
                    .doUpdate()
                    .set(item)
                    .returning()
                    .fetchOne();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean update(@NotNull Type item) {
        try (var connection = getConnection()) {
            return DSL.using(connection, SQLDialect.POSTGRES)
                    .executeUpdate(item) != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(@NotNull Type item) {
        try (var connection = getConnection()) {
            return DSL.using(connection, SQLDialect.POSTGRES)
                    .executeDelete(item) != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
