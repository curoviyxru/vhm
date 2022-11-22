package moe.crx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class Product {
    private int code;
    private String name;
}
