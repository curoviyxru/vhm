package moe.crx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import moe.crx.jooq.tables.records.ReceiptsRecord;

import java.sql.Date;

@Data
@AllArgsConstructor
public final class Receipt {
    private int id;
    private Date date;
    private int organizationId;

    public Receipt(ReceiptsRecord record) {
        this.id = record.getId();
        this.date = Date.valueOf(record.getDate());
        this.organizationId = record.getOrganizationId();
    }
}
