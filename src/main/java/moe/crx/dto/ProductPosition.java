package moe.crx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import moe.crx.jooq.tables.records.PositionsRecord;

@Data
@AllArgsConstructor
public final class ProductPosition {
    private int id;
    private int receiptId;
    private int productId;
    private double price;
    private int amount;

    public ProductPosition(PositionsRecord record) {
        this.id = record.getId();
        this.receiptId = record.getReceiptId();
        this.productId = record.getProductId();
        this.price = record.getPrice();
        this.amount = record.getAmount();
    }
}
