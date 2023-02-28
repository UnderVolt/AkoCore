package io.undervolt.cubecore.storage;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.common.io.ByteStreams;

public class DynamicResourceCache {
    private static final Map<String, byte[]> cache = new HashMap<>();

    public static BufferedInputStream get(String uuid) throws IOException {
        return new BufferedInputStream(new ByteArrayInputStream(cache.get(uuid)));
    }

    public static boolean has(String path) {
        return cache.containsKey(path);
    }

    public static void save(String path, InputStream stream) throws IOException {
        if(!cache.containsKey(path)){
            byte[] data = ByteStreams.toByteArray(stream);
            stream.close();
            cache.put(path, data);
        }
    }
}