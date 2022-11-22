package moe.crx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import moe.crx.jooq.tables.records.OrganizationsRecord;

@Data
@AllArgsConstructor
public final class Organization {
    private int inn;
    private String name;
    private String giro;

    public Organization(OrganizationsRecord record) {
        this.inn = record.getInn();
        this.name = record.getName();
        this.giro = record.getGiro();
    }
}
