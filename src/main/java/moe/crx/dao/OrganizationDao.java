package moe.crx.dao;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.jooq.tables.records.OrganizationsRecord;
import org.jetbrains.annotations.NotNull;

import static moe.crx.jooq.Tables.ORGANIZATIONS;

public final class OrganizationDao extends AbstractDao<OrganizationsRecord> {

    @Inject
    public OrganizationDao(@NotNull HikariDataSource dataSource) {
        super(dataSource, ORGANIZATIONS, ORGANIZATIONS.INN);
    }
}
