package io.undervolt.cubecore.api.events;

import io.undervolt.cubecore.packets.IPacket;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CustomPayloadEvent extends Event {

    public final IPacket packet;

    public CustomPayloadEvent(IPacket packet) {
        this.packet = packet;
    }
}