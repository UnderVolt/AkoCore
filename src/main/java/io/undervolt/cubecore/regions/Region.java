package io.undervolt.cubecore.regions;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.undervolt.cubecore.CubecoreMod;
import net.minecraft.util.math.Vec3d;

public class Region {

    private transient List<String> guests;
    private Vec3d[] points;
    private String id;

    public Region(String id, Vec3d[] points) {
        this.id = id;
        this.points = points;
        this.guests = Lists.newArrayList();
    }

    /**
     * @return Vec3d[] return the points
     */
    public Vec3d[] getPoints() {
        return points;
    }

    /**
     * @return String return the id
     */
    public String getID() {
        return id;
    }

    /**
     * @return List<String> return the guests
     */
    public List<String> getGuests() {
        return guests;
    }

    public JsonObject toJson(){
        Gson gson = CubecoreMod.network.gson;
        JsonElement element = gson.toJsonTree(Arrays.asList(this.points), new TypeToken<List<Vec3d>>() {}.getType());
        JsonObject tmp = new JsonObject();
        tmp.addProperty("id", id);
        tmp.add("points", element);
        return tmp;
    }

    public static Region fromJson(JsonObject obj){
        Gson gson = CubecoreMod.network.gson;
        String id = obj.get("id").getAsString();
        JsonElement element = obj.get("points");
        Vec3d[] points = gson.fromJson(element, Vec3d[].class);
        return new Region(id, points);
    }
}