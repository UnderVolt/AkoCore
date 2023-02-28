package io.undervolt.cubecore.net.adapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class JsonDeserializerWithInheritance<T> implements JsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Class<?> c;
        try {
            c = Class.forName(obj.get("type").getAsString());
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e.getMessage());
        }
        System.out.println("Deserializing: " + c.getName());
        return context.deserialize(obj, c);
    }
}