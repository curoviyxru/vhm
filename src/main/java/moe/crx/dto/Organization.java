package moe.crx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class Organization {
    private int inn;
    private String name;
    private String giro;
}
