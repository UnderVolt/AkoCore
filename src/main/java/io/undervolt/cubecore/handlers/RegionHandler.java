package io.undervolt.cubecore.handlers;

import io.undervolt.cubecore.packets.UseSndWandPacket;
import io.undervolt.cubecore.regions.Region;

import java.io.FileWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.undervolt.cubecore.CubecoreMod;
import io.undervolt.cubecore.api.events.CustomPayloadEvent;
import io.undervolt.cubecore.api.events.PlayerRegionEvent;
import io.undervolt.cubecore.items.SoundRegionWand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RegionHandler {

    public static List<Region> regions = Lists.newArrayList();
    Map<String, List<Vec3d>> watchers = Maps.newHashMap();

    @SubscribeEvent
    public void onMove(LivingUpdateEvent e){
        if(!(e.getEntity() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) e.getEntity();
        Vec3d pos = player.getPositionVector();
        String uuid = player.getUniqueID().toString();

        regions.forEach((region) -> {
            List<String> guests = region.getGuests();

            if(isInside(region.getPoints(), region.getPoints().length, pos)){
                if(!guests.contains(uuid)){
                    guests.add(uuid);
                    MinecraftForge.EVENT_BUS.post(new PlayerRegionEvent(player, region, PlayerRegionEvent.RegionAction.ENTER));
                }
                return;
            }
            if(guests.contains(uuid)){
                guests.remove(uuid);
                MinecraftForge.EVENT_BUS.post(new PlayerRegionEvent(player, region, PlayerRegionEvent.RegionAction.EXIT));
            }
        });
    }

    @SubscribeEvent
    public void onBreakBlock(BreakEvent event) {
        if(!(event.getPlayer().getHeldItemMainhand().getItem() instanceof SoundRegionWand)) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onPacket(CustomPayloadEvent event) {
        if(!(event.packet instanceof UseSndWandPacket)) return;
        EntityPlayer player = event.packet.getPlayer();
        String uuid = player.getUniqueID().toString();
        UseSndWandPacket data = (UseSndWandPacket) event.packet;
        Vec3d pos = data.pos;
        if(!watchers.containsKey(uuid))
            watchers.put(uuid, Lists.newArrayList());
        List<Vec3d> Vec3ds = watchers.get(uuid);

        int id = watchers.get(uuid).size();

        if(id > 0 && Vec3ds.get(0).x == pos.x && Vec3ds.get(0).z == pos.z) {
            player.sendMessage(new TextComponentString(String.format("Finished location definition with %d Vec3d", watchers.get(uuid).size())));
            Vec3d[] points = new Vec3d[Vec3ds.size()];
            watchers.get(uuid).toArray(points);
            regions.add(new Region(data.name, points));

            // Save to file
            try (Writer writer = new FileWriter("config/regions.json")) {
                Gson gson = CubecoreMod.network.gson;
                JsonObject obj = new JsonObject();
                JsonArray arr = new JsonArray();
                for(Region r : regions) {
                    arr.add(r.toJson());
                }
                obj.add("regions", arr);
                gson.toJson(obj, writer);
            } catch( Exception e) {
                e.printStackTrace();
            }
            watchers.remove(uuid);
            return;
        }

        player.sendMessage(new TextComponentString(String.format("Added Vec3d #%d at %s", id, data.pos.toString())));
        player.world.setBlockState(new BlockPos(data.pos), (id == 0 ? Blocks.REDSTONE_BLOCK : Blocks.STAINED_GLASS).getDefaultState());
        Vec3ds.add(data.pos);
    }

    //TODO: Clean this shitty code

    int INF = 10000;

    boolean onSegment(Vec3d p, Vec3d q, Vec3d r) {
        return q.x <= Math.max(p.x, r.x) &&
        q.x >= Math.min(p.x, r.x) &&
        q.z <= Math.max(p.z, r.z) &&
        q.z >= Math.min(p.z, r.z);
    }

    int orientation(Vec3d p, Vec3d q, Vec3d r) {
        double val = (q.z - p.z) * (r.x - q.x)
                - (q.x - p.x) * (r.z - q.z);

        if (val == 0) return 0;
        return (val > 0) ? 1 : 2;
    }

    boolean doIntersect(Vec3d p1, Vec3d q1, Vec3d p2, Vec3d q2) {
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4)
            return true;

        if (o1 == 0 && onSegment(p1, p2, q1))
            return true;

        if (o2 == 0 && onSegment(p1, q2, q1))
            return true;

        if (o3 == 0 && onSegment(p2, p1, q2))
            return true;

        if (o4 == 0 && onSegment(p2, q1, q2))
            return true;

        return false;
    }

    boolean isInside(Vec3d polygon[], int n, Vec3d p) {
        if (n < 3) return false;

        Vec3d extreme = new Vec3d(INF, 0, p.z);


        int count = 0, i = 0;
        do {
            int next = (i + 1) % n;
            if (doIntersect(polygon[i], polygon[next], p, extreme)) {
                if (orientation(polygon[i], p, polygon[next]) == 0)
                    return onSegment(polygon[i], p, polygon[next]);
                count++;
            }
            i = next;
        } while (i != 0);
        return (count % 2 == 1);
    }
}