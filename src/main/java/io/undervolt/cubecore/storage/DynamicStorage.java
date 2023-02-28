package io.undervolt.cubecore.storage;

import java.util.HashMap;
import java.util.Map;

public class DynamicStorage {
    private final static Map<String, Object> storage = new HashMap<>();

    public static void set(String key, Object value) {
        storage.put(key, value);
    }

    public static Object get(String key) {
        return storage.get(key);
    }
}