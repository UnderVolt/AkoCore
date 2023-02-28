package io.undervolt.cubecore.net;

import java.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import io.undervolt.cubecore.CubecoreMod;
import io.undervolt.cubecore.api.events.CustomPayloadEvent;
import io.undervolt.cubecore.packets.IPacket;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;

public class CustomPayloadPacket implements IMessage {

    private String rawJson;

    public CustomPayloadPacket() {}

    public CustomPayloadPacket( String rawJson ) {
        this.rawJson = rawJson;
    }

    @Override
    public void fromBytes(ByteBuf buf){
        this.rawJson = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf){
        ByteBufUtils.writeUTF8String(buf, this.rawJson);
    }

    public static class Handler implements IMessageHandler<CustomPayloadPacket, IMessage> {

        @Override
        public IMessage onMessage(CustomPayloadPacket message, MessageContext ctx) {
            Gson gson = CubecoreMod.network.gson;
            IPacket packet = gson.fromJson(new String(Base64.getDecoder().decode(message.rawJson)), IPacket.class);
            MinecraftForge.EVENT_BUS.post(new CustomPayloadEvent(packet));
            return null;
        }
    }
}