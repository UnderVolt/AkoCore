package io.undervolt.cubecore.registry;

import java.util.List;

import com.google.common.collect.Lists;

import io.undervolt.cubecore.blocks.RGBBlock;
import io.undervolt.cubecore.items.SoundRegionWand;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class CubecoreRegistry {

    private static List<Object> entries = Lists.newArrayList();

    public static void init() {
        entries.add(new RGBBlock());
        entries.add(new SoundRegionWand());
    }

    public static void registerEntries() {
        entries.forEach(CubecoreRegistry::register);
    }

    public static void registerRenders() {
        entries.forEach(CubecoreRegistry::registerRender);
    }

    public static void register(Object obj){
        if(obj instanceof Block){
            Block block = (Block) obj;
            ItemBlock item = new ItemBlock(block);
            item.setRegistryName(block.getRegistryName());
            ForgeRegistries.BLOCKS.register(block);
            ForgeRegistries.ITEMS.register(item);
        }
        if(obj instanceof Item){
            ForgeRegistries.ITEMS.register((Item) obj);
        }
    }

    public static void registerRender(Object obj){
        Item item = obj instanceof Block ? Item.getItemFromBlock((Block) obj) : (Item) obj;
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation("cubecore:" + item.getUnlocalizedName().substring(5), "inventory"));
        return;
        // if(obj instanceof Block){
        // } else {
        //     ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation("cubecore:" + item.getUnlocalizedName().substring(5), "inventory"));
        // }
    }
}