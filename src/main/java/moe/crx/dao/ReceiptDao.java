package moe.crx.dao;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.jooq.tables.records.ReceiptsRecord;
import org.jetbrains.annotations.NotNull;

import static moe.crx.jooq.Tables.RECEIPTS;

public final class ReceiptDao extends AbstractDao<ReceiptsRecord> {

    @Inject
    public ReceiptDao(@NotNull HikariDataSource dataSource) {
        super(dataSource, RECEIPTS, RECEIPTS.ID);
    }
}
