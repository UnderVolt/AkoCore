package io.undervolt.cubecore.packets;

import java.util.UUID;

import com.google.gson.annotations.SerializedName;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public abstract class IPacket {

    @SerializedName("type")
    public String type;
    public String uuid;

    public IPacket() {
        this.type = this.getClass().getName();
    }

    public EntityPlayer getPlayer() {
        if(this.uuid == null) return null;
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            return null;

        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(UsernameCache.getLastKnownUsername(UUID.fromString(this.uuid)));
    }

}