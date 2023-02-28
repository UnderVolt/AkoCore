package io.undervolt.cubecore.packets;

import net.minecraft.util.math.Vec3d;

public class PlayerStatusPacket extends IPacket {

    private String sound;
    private String command;
    private Vec3d position;

    public PlayerStatusPacket(String sound, String command, Vec3d position) {
        this.sound = sound;
        this.command = command.toString();
    }

    public PlayerStatusPacket(String sound, String command) {
        this(sound, command, Vec3d.ZERO);
    }

    public String getSound() {
        return sound;
    }

    public String getCommand() {
        return command;
    }

    public static enum PlayerCommand {
        PLAY("play"),
        STOP("stop");

        private String command;

        private PlayerCommand(String command) {
            this.command = command;
        }

        public String toString(){
            return command;
        }
    }
}