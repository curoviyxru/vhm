package moe.crx.dao;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import moe.crx.dto.Organization;
import moe.crx.jooq.tables.records.OrganizationsRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static moe.crx.jooq.Tables.ORGANIZATIONS;

public final class OrganizationDao extends AbstractDao<Organization, OrganizationsRecord, Integer> {

    @Inject
    public OrganizationDao(@NotNull HikariDataSource dataSource) {
        super(Organization.class, dataSource, ORGANIZATIONS, ORGANIZATIONS.ID, true, ORGANIZATIONS.NAME);
    }

    public @Nullable Organization readByName(String name) {
        try (var c = getConnection()) {
            return c.context()
                    .fetchOptional(ORGANIZATIONS, ORGANIZATIONS.NAME.eq(name))
                    .map(r -> r.into(Organization.class))
                    .orElse(null);
        }
    }
}
