package moe.crx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import moe.crx.jooq.tables.records.ProductsRecord;

@Data
@AllArgsConstructor
public final class Product {
    private int code;
    private String name;

    public Product(ProductsRecord record) {
        this.code = record.getCode();
        this.name = record.getName();
    }
}
