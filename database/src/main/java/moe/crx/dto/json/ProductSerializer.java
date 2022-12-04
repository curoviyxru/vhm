package moe.crx.dto.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import moe.crx.dto.Product;

import java.io.IOException;

public final class ProductSerializer extends StdSerializer<Product> {

    public ProductSerializer() {
        super(Product.class);
    }

    @Override
    public void serialize(Product value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("name", value.getName());
        gen.writeNumberField("org_id", value.getOrgId());
        gen.writeNumberField("amount", value.getAmount());
        gen.writeEndObject();
    }
}
