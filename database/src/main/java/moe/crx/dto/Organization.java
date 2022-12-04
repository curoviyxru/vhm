package moe.crx.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import moe.crx.dto.json.OrganizationDeserializer;
import moe.crx.dto.json.OrganizationSerializer;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = OrganizationSerializer.class)
@JsonDeserialize(using = OrganizationDeserializer.class)
public final class Organization {
    private int id;
    private String name;
}
