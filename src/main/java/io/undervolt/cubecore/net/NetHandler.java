package io.undervolt.cubecore.net;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.undervolt.cubecore.net.adapters.JsonDeserializerWithInheritance;
import io.undervolt.cubecore.net.adapters.Vec3DAdapter;
import io.undervolt.cubecore.packets.IPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class NetHandler {

    public Gson gson;
    public SimpleNetworkWrapper network;
    public Encoder encoder = Base64.getEncoder();
    public Decoder decoder = Base64.getDecoder();

    public void init(){
        this.gson = new GsonBuilder()
            .registerTypeAdapter(IPacket.class, new JsonDeserializerWithInheritance<IPacket>())
            .registerTypeAdapter(Vec3d.class, new Vec3DAdapter())
            .create();
        this.network = NetworkRegistry.INSTANCE.newSimpleChannel("cubecore");
        this.network.registerMessage(CustomPayloadPacket.Handler.class, CustomPayloadPacket.class, 0, Side.CLIENT);
        this.network.registerMessage(CustomPayloadPacket.Handler.class, CustomPayloadPacket.class, 0, Side.SERVER);
    }

    public void sendToServer(IPacket packet){
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if(player != null)
            packet.uuid = player.getUniqueID().toString();
        this.network.sendToServer(new CustomPayloadPacket(encoder.encodeToString(this.gson.toJson(packet).getBytes())));
    }

    public void sendToPlayer(IPacket packet, EntityPlayerMP player){
        this.network.sendTo(new CustomPayloadPacket(encoder.encodeToString(gson.toJson(packet).getBytes())), player);
    }

    public void sendToAll(IPacket packet){
        this.network.sendToAll(new CustomPayloadPacket(encoder.encodeToString(gson.toJson(packet).getBytes())));
    }
}