package moe.crx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public final class Receipt {
    private int id;
    private Date date;
    private int organizationId;
}
