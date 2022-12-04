package moe.crx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public final class ProductList {
    private final List<Product> products;
    private final List<Organization> organizations;
}
