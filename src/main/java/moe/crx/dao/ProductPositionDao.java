package moe.crx.dao;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.jooq.tables.records.PositionsRecord;
import org.jetbrains.annotations.NotNull;

import static moe.crx.jooq.Tables.POSITIONS;

public final class ProductPositionDao extends AbstractDao<PositionsRecord> {

    @Inject
    public ProductPositionDao(@NotNull HikariDataSource dataSource) {
        super(dataSource, POSITIONS, POSITIONS.ID, false);
    }
}
