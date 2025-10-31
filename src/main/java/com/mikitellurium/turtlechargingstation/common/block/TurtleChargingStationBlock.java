package com.mikitellurium.turtlechargingstation.common.block;

import com.mikitellurium.turtlechargingstation.client.ModGuiHandler;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationTileEntity;
import com.mikitellurium.turtlechargingstation.networking.ModNetworking;
import com.mikitellurium.turtlechargingstation.networking.packet.EnergySyncS2CPacket;
import com.mikitellurium.turtlechargingstation.registry.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class TurtleChargingStationBlock extends Block {
    public static final PropertyBool ENABLED = PropertyBool.create("enabled");
    public static final PropertyBool CHARGING = PropertyBool.create("charging");

    public TurtleChargingStationBlock() {
        super(Material.ROCK, MapColor.BLACK);
        this.setCreativeTab(ModCreativeTabs.MAIN_TAB);
        this.setHardness(3.0F).setResistance(6.0F).setHarvestLevel("pickaxe", 0);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ENABLED, true).withProperty(CHARGING, false));
    }

    @Override
    public SoundType getSoundType() {
        return SoundType.METAL;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TurtleChargingStationTileEntity();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ENABLED, CHARGING);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(ENABLED, true);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean enabled = (meta & 1) != 0;
        boolean charging = (meta & 2) != 0;

        return this.getDefaultState().withProperty(ENABLED, enabled).withProperty(CHARGING, charging);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;

        if (state.getValue(ENABLED)) meta |= 1;
        if (state.getValue(CHARGING)) meta |= 2;

        return meta;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TurtleChargingStationTileEntity) {
                TurtleChargingStationTileEntity stationTile = (TurtleChargingStationTileEntity) tile;
                ModGuiHandler.openTurtleChargingStationGui(playerIn, stationTile);
                ModNetworking.INSTANCE.sendToAll(new EnergySyncS2CPacket(pos, stationTile.getEnergy()));
            } else {
                throw new IllegalStateException("Container provider is missing");
            }
        }
        return true;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        //if (!oldState.is(blockState.getBlock())) {
            this.checkPoweredState(world, pos, state);
        //}
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        this.checkPoweredState(world, pos, state);
    }

    private void checkPoweredState(World world, BlockPos pos, IBlockState state) {
        boolean flag = !world.isBlockPowered(pos);
        if (flag != state.getValue(ENABLED)) {
            world.setBlockState(pos, state.withProperty(ENABLED, flag), 2);
        }
    }
}
