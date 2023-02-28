package io.undervolt.cubecore.resource;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResource;
import io.undervolt.cubecore.storage.DynamicStorage;
import io.undervolt.cubecore.storage.DynamicResourceCache;
import net.minecraft.client.resources.data.IMetadataSection;

public class CubecoreDynamicResource implements IResource {

    BufferedInputStream stream;
    private String uuid;

    public CubecoreDynamicResource(String uuid){
        this.uuid = uuid;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return null;
    }

    @Override
    public InputStream getInputStream() {
        Object data = DynamicStorage.get(uuid);
        try {
            if(!DynamicResourceCache.has(uuid)) {
                InputStream dataObj = null;
                if(data instanceof String){
                    HttpURLConnection http = (HttpURLConnection) new URL((String) data).openConnection();
                    http.addRequestProperty("User-Agent", "Mozilla/4.76");
                    dataObj = http.getInputStream();
                }
                if(data instanceof byte[]){
                    dataObj = new BufferedInputStream(new java.io.ByteArrayInputStream((byte[]) data));
                }
                DynamicResourceCache.save(uuid, dataObj);
            }
            this.stream = DynamicResourceCache.get(uuid);
            return this.stream;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean hasMetadata() {
        return false;
    }

    @Override
    public <T extends IMetadataSection> T getMetadata(String p_110526_1_) {
        return null;
    }

    @Override
    public String getResourcePackName() {
        return null;
    }

    @Override
    public void close() throws IOException {
        if(this.stream != null) this.stream.close();
    }
}