package com.mikitellurium.turtlechargingstation.common.blockentity;

import com.mikitellurium.turtlechargingstation.common.block.TurtleChargingStationBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TurtleChargingStationTileEntity extends TileEntity implements ITickable {
    public void switchState(IBlockState state) {
        boolean charging = state.getValue(TurtleChargingStationBlock.CHARGING);
        world.setBlockState(pos, state.withProperty(TurtleChargingStationBlock.CHARGING, !charging));
        System.out.printf("Changed blockstate charging property to " + !charging);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        System.out.println("Triggered tile refresh at " + pos);
        return newState.getBlock() != oldState.getBlock();
    }
}
