package io.undervolt.cubecore.sound;

import java.util.Map;

import io.undervolt.cubecore.CubecoreMod;
import io.undervolt.cubecore.api.events.PlayerRegionEvent;
import io.undervolt.cubecore.api.events.PlayerRegionEvent.RegionAction;
import io.undervolt.cubecore.packets.PlayerStatusPacket;
import io.undervolt.cubecore.proxy.ServerProxy;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SoundHandler {

    @SubscribeEvent
    public void onRegion(PlayerRegionEvent event){
        Map<String, String> map = ServerProxy.musicRegions;
        String uuid = event.getRegion().getID();
        if(map.containsKey(uuid)){
            CubecoreMod.network.sendToPlayer(new PlayerStatusPacket(map.get(uuid), event.getAction() == RegionAction.ENTER ? "play" : "stop"), (EntityPlayerMP) event.getPlayer());
        }
    }
}