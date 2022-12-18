package moe.crx.api;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Jsonable<T extends Jsonable<?>> {

    public @NotNull T fromJson(@Nullable JsonObject object) {
        if (object == null) return (T) this;
        return (T) object.mapTo(getClass());
    }

    public @NotNull JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }
}
