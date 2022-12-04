package moe.crx.dto.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import moe.crx.dto.Organization;

import java.io.IOException;

public final class OrganizationSerializer extends StdSerializer<Organization> {

    public OrganizationSerializer() {
        super(Organization.class);
    }

    @Override
    public void serialize(Organization value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("name", value.getName());
        gen.writeEndObject();
    }
}
