package io.undervolt.cubecore.api.events;

import io.undervolt.cubecore.regions.Region;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerRegionEvent extends Event {

    private EntityPlayer player;
    private Region region;
    private RegionAction action;

    public PlayerRegionEvent(EntityPlayer player, Region region, RegionAction action) {
        this.player = player;
        this.region = region;
        this.action = action;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public Region getRegion() {
        return region;
    }

    public RegionAction getAction() {
        return action;
    }

    public static enum RegionAction {
        ENTER,
        EXIT;
    }
}