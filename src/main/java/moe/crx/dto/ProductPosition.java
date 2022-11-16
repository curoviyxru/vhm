package moe.crx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class ProductPosition {
    private int id;
    private int receiptId;
    private int productId;
    private double price;
    private int amount;
}
