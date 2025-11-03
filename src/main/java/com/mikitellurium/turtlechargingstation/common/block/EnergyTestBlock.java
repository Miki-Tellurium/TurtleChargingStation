package com.mikitellurium.turtlechargingstation.common.block;

import com.mikitellurium.turtlechargingstation.common.blockentity.EnergyTestTile;
import com.mikitellurium.turtlechargingstation.registry.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EnergyTestBlock extends Block {
    public EnergyTestBlock() {
        super(Material.ROCK, MapColor.BLACK);
        this.setCreativeTab(ModCreativeTabs.MAIN_TAB);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new EnergyTestTile();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this);
    }
}
