package io.undervolt.cubecore.net.adapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import io.undervolt.cubecore.CubecoreSettings;
import net.minecraft.util.math.Vec3d;

public class Vec3DAdapter implements JsonDeserializer<Vec3d> {

    @Override
    public Vec3d deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jObj = json.getAsJsonObject();
        return new Vec3d(jObj.get( CubecoreSettings.RELEASE ? "field_72450_a" : "x" ).getAsDouble(), jObj.get( CubecoreSettings.RELEASE ? "field_72448_b" : "y" ).getAsDouble(), jObj.get( CubecoreSettings.RELEASE ? "field_72449_c" : "z" ).getAsDouble());
    }

}