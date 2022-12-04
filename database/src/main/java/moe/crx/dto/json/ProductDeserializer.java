package moe.crx.dto.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import moe.crx.dto.Product;

import java.io.IOException;

public final class ProductDeserializer extends StdDeserializer<Product> {

    public ProductDeserializer() {
        super(Product.class);
    }

    @Override
    public Product deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode tree = p.readValueAsTree();
        return new Product(
                tree.get("id").asInt(),
                tree.get("name").asText(),
                tree.get("org_id").asInt(),
                tree.get("amount").asInt()
        );
    }
}
