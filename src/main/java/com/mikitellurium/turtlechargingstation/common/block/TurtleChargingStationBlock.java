package com.mikitellurium.turtlechargingstation.common.block;

import com.mikitellurium.turtlechargingstation.registry.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class TurtleChargingStationBlock extends Block {
    public static final PropertyBool ENABLED = PropertyBool.create("enabled");
    public static final PropertyBool CHARGING = PropertyBool.create("charging");

    public TurtleChargingStationBlock() {
        super(Material.ROCK, MapColor.BLACK);
        this.setCreativeTab(ModCreativeTabs.MAIN_TAB);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(ENABLED, false)
                .withProperty(CHARGING, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ENABLED, CHARGING);
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
}
