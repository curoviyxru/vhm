package moe.crx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public final class OrganizationProductList {
    private final List<Product> products;
}
