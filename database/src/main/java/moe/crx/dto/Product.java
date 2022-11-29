package moe.crx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import moe.crx.jooq.tables.records.ProductsRecord;

@Data
@AllArgsConstructor
public final class Product {
    private int id;
    private String name;
    private int orgId;
    private int amount;

    public Product(ProductsRecord record) {
        this.id = record.getId();
        this.name = record.getName();
        this.orgId = record.getOrgId();
        this.amount = record.getAmount();
    }
}
