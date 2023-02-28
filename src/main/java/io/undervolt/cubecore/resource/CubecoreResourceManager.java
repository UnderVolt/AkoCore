package io.undervolt.cubecore.resource;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.FallbackResourceManager;

import java.util.Set;
import java.util.List;
import java.nio.file.Paths;
import org.apache.commons.io.FilenameUtils;
import net.minecraft.client.resources.data.MetadataSerializer;

public class CubecoreResourceManager extends FallbackResourceManager {

    public CubecoreResourceManager(MetadataSerializer frmMetadataSerializerIn) {
        super(frmMetadataSerializerIn);
    }

    @Override
    public Set<String> getResourceDomains() {
        return null;
    }

    @Override
    public IResource getResource(ResourceLocation location) {
        String uuid = FilenameUtils.getBaseName(Paths.get(location.getResourcePath()).getFileName().toString());
        return new CubecoreDynamicResource(uuid);
    }

    @Override
    public List<IResource> getAllResources(ResourceLocation location) {
        return null;
    }
}