package moe.crx.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import moe.crx.dto.json.ProductDeserializer;
import moe.crx.dto.json.ProductSerializer;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = ProductSerializer.class)
@JsonDeserialize(using = ProductDeserializer.class)
public final class Product {
    private int id;
    private String name;
    private int orgId;
    private int amount;
}
