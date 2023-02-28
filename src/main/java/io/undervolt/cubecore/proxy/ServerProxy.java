package io.undervolt.cubecore.proxy;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.undervolt.cubecore.CubecoreMod;
import io.undervolt.cubecore.handlers.RegionHandler;
import io.undervolt.cubecore.models.DynamicSound;
import io.undervolt.cubecore.packets.RegisterSoundPacket;
import io.undervolt.cubecore.regions.Region;
import io.undervolt.cubecore.sound.SoundHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class ServerProxy implements CommonProxy {

    public Configuration config;
    public static String[] soundList;
    public static Map<String, String> musicRegions = Maps.newHashMap();


    @Override
    public void preinit() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new RegionHandler());
        MinecraftForge.EVENT_BUS.register(new SoundHandler());
    }

    @Override
    public void init() {
        File file = new File("config/undervolt.cfg");
        this.config = new Configuration(file);
        config.load();
        soundList  = config.getStringList("soundList", "regions", new String[] {"doom|0.5|http://moon64.undervolt.io/share/doom.ogg"}, "The registered sounds to load when joining");
        String[] regionList = config.getStringList("regionList", "regions", new String[] {"doomHouse|doom"}, "Region sounds");
        config.save();

        for (String region : regionList) {
            String[] split = region.split("\\|");
            musicRegions.put(split[0], split[1]);
        }

        File regs = new File("config/regions.json");
        if(regs.exists()){
            try {
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(new FileReader(regs));
                JsonObject jObj = jsonElement.getAsJsonObject();
                jObj.get("regions").getAsJsonArray().forEach(region -> {
                    Region r = Region.fromJson(region.getAsJsonObject());
                    RegionHandler.regions.add(r);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void onJoin(PlayerLoggedInEvent event) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Arrays.stream(ServerProxy.soundList).forEach((ev) -> {
                    DynamicSound sound = new DynamicSound();
                    String[] split = ev.split("\\|");
                    System.out.println("Sending:" + ev + " data: " + split[0] + " " + split[1] + " " + split[2]);
                    sound.name = split[0];
                    sound.replace = true;
                    sound.sounds.add(
                        new DynamicSound.Data()
                            .setVolume(Float.parseFloat(split[1]))
                            .setUrl(split[2])
                            .setStream(true)
                    );
                    CubecoreMod.network.sendToPlayer(new RegisterSoundPacket(sound), (EntityPlayerMP) event.player);
                });
            }
        }, 1000);
    }
}