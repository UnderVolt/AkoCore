package io.undervolt.cubecore.models;

import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

public class DynamicSound {

    private static final Gson gson = new Gson();
    public String name;
    public boolean replace;
    public List<Data> sounds = Lists.newArrayList();

    public static class Data {
        @SerializedName("name")
        public String path = "dccore:internal/"+UUID.randomUUID().toString();
        public String url;
        public boolean stream = true;
        public float volume = 1.0f;
        public float pitch = 1.0f;

        public Data setPath(String path) {
            this.path = path;
            return this;
        }

        public Data setUrl(String url) {
            this.url = url;
            return this;
        }

        public Data setStream(boolean stream) {
            this.stream = stream;
            return this;
        }

        public Data setVolume(float volume) {
            this.volume = volume;
            return this;
        }

        public Data setPitch(float pitch) {
            this.pitch = pitch;
            return this;
        }
    }

    public static DynamicSound parse(String json){
        JsonObject raw = new JsonParser().parse(json).getAsJsonObject();
        Entry<String, JsonElement> data = Lists.newArrayList(raw.entrySet()).get(0);
        DynamicSound tmp = new DynamicSound();
        JsonObject root = data.getValue().getAsJsonObject();
        tmp.name = data.getKey();
        if(root.has("replace"))
            tmp.replace = root.get("replace").getAsBoolean();
        root.get("sounds").getAsJsonArray().forEach(element -> {
            tmp.sounds.add(gson.fromJson(element, Data.class));
        });
        return tmp;
    }

    public String toJson(){
        JsonObject obj = new JsonObject();
        JsonObject def = new JsonObject();
        if(this.replace) def.addProperty("replace", this.replace);
        def.add("sounds", gson.toJsonTree(this.sounds).getAsJsonArray());
        obj.add(name, def);
        return gson.toJson(obj);
    }

}