package moe.crx.verticles.codec;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

public final class JsonMessageCodec implements MessageCodec<Object, Object> {

    private static final String CLASS_TYPE = "classType";

    @Override
    public void encodeToWire(Buffer buffer, Object obj) {
        var string = new JsonObject()
                .put(CLASS_TYPE, obj.getClass().getName())
                .put(obj.getClass().getName(), JsonObject.mapFrom(obj))
                .encode();

        buffer.appendInt(string.getBytes().length);
        buffer.appendString(string);
    }

    @Override
    public Object decodeFromWire(int pos, Buffer buffer) {
        var length = buffer.getInt(pos);
        var json = new JsonObject(buffer.getString(pos += 4, pos + length));
        var classType = json.getString(CLASS_TYPE);

        try {
            return json.getJsonObject(classType).mapTo(Class.forName(classType));
        } catch (Exception ignored) {
        }

        return null;
    }

    @Override
    public Object transform(Object obj) {
        return obj;
    }

    @Override
    public String name() {
        return this.getClass().getName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
