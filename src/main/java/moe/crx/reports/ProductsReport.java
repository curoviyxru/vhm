package moe.crx.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import moe.crx.jooq.tables.records.ProductsRecord;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public final class ProductsReport {
    private final Map<LocalDate, Map<ProductsRecord, ProductSummary>> perDay;
    private final Map<ProductsRecord, ProductSummary> inPeriod;

    public ProductsReport() {
        this.perDay = new HashMap<>();
        this.inPeriod = new HashMap<>();
    }
}
