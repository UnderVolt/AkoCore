package io.undervolt.cubecore.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class RGBBlock extends Block {
    public RGBBlock() {
        super(Material.CLAY);
        this.setUnlocalizedName("rgbblock");
        this.setRegistryName(new ResourceLocation("cubecore", "rgbblock"));
    }
}