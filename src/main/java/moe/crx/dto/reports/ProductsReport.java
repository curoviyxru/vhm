package moe.crx.dto.reports;

import lombok.Data;
import lombok.NoArgsConstructor;
import moe.crx.dto.Product;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public final class ProductsReport {
    private final Map<Date, Map<Product, ProductSummary>> perDay = new HashMap<>();
    private final Map<Product, ProductSummary> inPeriod = new HashMap<>();
}
