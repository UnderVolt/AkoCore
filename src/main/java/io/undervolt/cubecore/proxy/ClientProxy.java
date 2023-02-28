package io.undervolt.cubecore.proxy;

import io.undervolt.cubecore.net.NetHandler;
import io.undervolt.cubecore.packets.PlayerStatusPacket;
import io.undervolt.cubecore.packets.RegisterSoundPacket;
import io.undervolt.cubecore.registry.CubecoreRegistry;
import io.undervolt.cubecore.sound.SoundHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import io.undervolt.cubecore.api.events.CustomPayloadEvent;
import io.undervolt.cubecore.handlers.DynamicResourceHandler;


public class ClientProxy implements CommonProxy{

    private DynamicResourceHandler tmp;
    private Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void preinit() {
        OBJLoader.INSTANCE.addDomain("cubecore");
    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new NetHandler());
        MinecraftForge.EVENT_BUS.register(new SoundHandler());
        MinecraftForge.EVENT_BUS.register(this.tmp = new DynamicResourceHandler());

        CubecoreRegistry.registerRenders();
    }

    @SubscribeEvent
    public void onSoundPlayed(PlaySoundEvent event) {
        if ( event.getSound().getSoundLocation().getResourcePath().contains("music") && event.getSound().getCategory() == SoundCategory.MUSIC ) {
            event.setResultSound(null);
        }
    }

    @SubscribeEvent
    public void onPacket(CustomPayloadEvent ev){
        if(ev.packet instanceof RegisterSoundPacket) {
            this.tmp.registerSound(((RegisterSoundPacket) ev.packet).getSound());
            System.out.println("Client received a sound register packet");
        }

        if(ev.packet instanceof PlayerStatusPacket){
            PlayerStatusPacket packet = (PlayerStatusPacket) ev.packet;
            switch(packet.getCommand()){
                case "play":
                    this.mc.getSoundHandler().stopSound(PositionedSoundRecord.getMusicRecord(new SoundEvent(new ResourceLocation("music.game"))));
                    this.mc.getSoundHandler().stopSound(PositionedSoundRecord.getMusicRecord(new SoundEvent(new ResourceLocation("music.game.creative"))));
                    this.mc.getSoundHandler().stopSound(PositionedSoundRecord.getMusicRecord(new SoundEvent(new ResourceLocation("music.game.nether"))));
                    this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMusicRecord(new SoundEvent(new ResourceLocation(packet.getSound()))));
                    break;
                case "stop":
                    this.mc.getSoundHandler().stop(packet.getSound(), SoundCategory.MUSIC);
                    break;
            }
        }
    }
}