package io.undervolt.cubecore.handlers;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Map.Entry;

import java.io.InputStream;
import java.util.LinkedList;
import java.nio.charset.Charset;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.SoundEvent;
import org.apache.commons.io.IOUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.audio.SoundList;
import net.minecraft.client.audio.SoundHandler;
import io.undervolt.cubecore.models.DynamicSound;
import net.minecraftforge.registries.ForgeRegistry;
import io.undervolt.cubecore.storage.DynamicStorage;
import io.undervolt.cubecore.utils.ReflectionUtil;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import io.undervolt.cubecore.resource.CubecoreResourceManager;
import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.resources.SimpleReloadableResourceManager;

public class DynamicResourceHandler {

    private boolean loading = false;
    private SoundHandler soundHandler;
    private MetadataSerializer metadataSerializer;
    private final Minecraft mc = Minecraft.getMinecraft();
    private HashMap<String, SoundEvent> soundEntries = new HashMap<>();
    private Map<String, FallbackResourceManager> domainResourceManagers;
    private final ForgeRegistry<SoundEvent> registry = (ForgeRegistry<SoundEvent>) ForgeRegistries.SOUND_EVENTS;

    public DynamicResourceHandler() {
        this.metadataSerializer = (MetadataSerializer) ReflectionUtil.getField(this.mc, Minecraft.class,
                "metadataSerializer_", "field_110452_an");
        this.soundHandler = (SoundHandler) ReflectionUtil.getField(this.mc, Minecraft.class, "mcSoundHandler",
                "field_147127_av");
    }

    public void registerSound(DynamicSound sound) {
        if (this.soundEntries.containsKey(sound.name) && !sound.replace) {
            System.out.println("[CubeCore] Sound " + sound.name + " already registered!");
            return;
        }
        if (this.loading) {
            System.out.println("[Cubecore] Register in progress");
            return;
        }

        Minecraft.getMinecraft().player.sendMessage(new TextComponentString("[CubeCore] Downloading sound: '" + sound.name + "'"));

        sound.sounds.forEach((data) -> {
            if (data.url != null) {
                DynamicStorage.set(data.path.split("dccore:internal/")[1], data.url);
            }
        });

        this.loading = true;
        this.registry.unfreeze();
        List<Tuple<ResourceLocation, SoundList>> resources = new LinkedList<>();
        try {
            Object soundMapObj = ReflectionUtil
                    .getMethod(SoundHandler.class, "getSoundMap", "func_175085_a", Map.class, InputStream.class)
                    .invoke(this.soundHandler, IOUtils.toInputStream(sound.toJson(), Charset.defaultCharset()));
            for (Entry<String, SoundList> entry : ((Map<String, SoundList>) soundMapObj).entrySet()) {
                ResourceLocation location = new ResourceLocation(entry.getKey());
                resources.add(new Tuple<ResourceLocation, SoundList>(location, entry.getValue()));
                SoundEvent ev = new SoundEvent(location);
                ev.setRegistryName(location);
                ForgeRegistries.SOUND_EVENTS.register(ev);
                if (this.soundEntries.containsKey(sound.name))
                    this.soundEntries.remove(sound.name);
                this.soundEntries.put(sound.name, ev);
            }

            resources.forEach(entry -> {
                try {
                    ReflectionUtil
                            .getMethod(SoundHandler.class, "loadSoundResource", "func_147693_a", Void.class,
                                    ResourceLocation.class, SoundList.class)
                            .invoke(this.soundHandler, entry.getFirst(), entry.getSecond());
                } catch (Exception e) {
                    System.out.println("Invalid sounds.json");
                    System.out.println(e.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.registry.freeze();
        this.loading = false;
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        try {
            this.domainResourceManagers = (Map) ReflectionUtil.getField(this.mc.getResourceManager(), SimpleReloadableResourceManager.class, "domainResourceManagers", "field_110548_a");
        } catch (Exception e) {
            System.out.println("Failed to get domainResourceManagers | MetadataSerializer");
            System.out.println(e.getMessage());
        }
        if (domainResourceManagers == null || domainResourceManagers.containsKey("dccore"))
            return;
        domainResourceManagers.put("dccore", new CubecoreResourceManager(this.metadataSerializer));
        System.out.println("Cubecore Resource Manager Loaded");
    }

    public SoundEvent getSound(String name) {
        return this.soundEntries.get(name);
    }
}