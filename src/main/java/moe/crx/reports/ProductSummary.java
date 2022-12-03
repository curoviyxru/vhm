package moe.crx.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import moe.crx.jooq.tables.records.PositionsRecord;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class ProductSummary {
    private int amount;
    private double sum;

    public ProductSummary(PositionsRecord record) {
        this.amount = record.getAmount();
        this.sum = record.getPrice() * record.getAmount();
    }

    public ProductSummary(ProductSummary a, ProductSummary b) {
        this.amount = a.getAmount() + b.getAmount();
        this.sum = a.getSum() + b.getSum();
    }

    public void add(ProductSummary o) {
        this.amount += o.getAmount();
        this.sum += o.getSum();
    }
}
