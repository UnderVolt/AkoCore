package io.undervolt.cubecore.packets;

import net.minecraft.util.math.Vec3d;

public class UseSndWandPacket extends IPacket {

    public Vec3d pos;
    public String name;

    public UseSndWandPacket(Vec3d pos, String name) {
        this.pos = pos;
        this.name = name;
    }
}