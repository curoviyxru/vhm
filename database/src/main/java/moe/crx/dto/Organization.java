package moe.crx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import moe.crx.jooq.tables.records.OrganizationsRecord;

@Data
@AllArgsConstructor
public final class Organization {
    private int id;
    private String name;

    public Organization(OrganizationsRecord record) {
        this.id = record.getId();
        this.name = record.getName();
    }
}
