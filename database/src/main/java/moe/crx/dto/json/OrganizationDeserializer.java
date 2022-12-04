package moe.crx.dto.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import moe.crx.dto.Organization;

import java.io.IOException;

public final class OrganizationDeserializer extends StdDeserializer<Organization> {

    public OrganizationDeserializer() {
        super(Organization.class);
    }

    @Override
    public Organization deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode tree = p.readValueAsTree();
        return new Organization(
                tree.get("id").asInt(),
                tree.get("name").asText()
        );
    }
}
