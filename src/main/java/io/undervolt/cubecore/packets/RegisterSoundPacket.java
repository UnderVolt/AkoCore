package io.undervolt.cubecore.packets;

import io.undervolt.cubecore.models.DynamicSound;

public class RegisterSoundPacket extends IPacket {
    private DynamicSound sound;

    public RegisterSoundPacket(DynamicSound sound) {
        this.sound = sound;
    }

    public DynamicSound getSound() {
        return sound;
    }
}