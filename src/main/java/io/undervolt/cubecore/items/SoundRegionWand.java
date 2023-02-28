package io.undervolt.cubecore.items;

import java.util.UUID;

import com.google.gson.Gson;

import io.undervolt.cubecore.CubecoreMod;
import io.undervolt.cubecore.packets.UseSndWandPacket;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SoundRegionWand extends ItemPickaxe {

    public SoundRegionWand() {
        super(ToolMaterial.DIAMOND);
        this.setUnlocalizedName("sndrgwand");
        this.setRegistryName("sndrgwand");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(world.isRemote){
            UseSndWandPacket packet = new UseSndWandPacket(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), playerIn.getHeldItemMainhand().getDisplayName());
            CubecoreMod.network.sendToServer(packet);
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        target.setCustomNameTag("Dinnerbone");
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return false;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, EntityPlayer player, IBlockState blockState) {
        return -99;
    }
}