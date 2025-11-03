package com.mikitellurium.turtlechargingstation.common.blockentity;

import com.mikitellurium.telluriumforge.tileentity.ISideTickingTile;
import com.mikitellurium.turtlechargingstation.TurtleChargingStationMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

@Config(modid = TurtleChargingStationMod.MOD_ID)
public class EnergyTestTile extends TileEntity implements ISideTickingTile {
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return newState.getBlock() != oldState.getBlock();
    }

    @Override
    public void update() {
        this.tickTile(this);
    }

    @Override
    public void tickServer(WorldServer world, IBlockState state, BlockPos pos) {
        for (EnumFacing direction : EnumFacing.values()) {
            TileEntity tile = world.getTileEntity(pos.offset(direction));
            if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, direction.getOpposite())) {
                IEnergyStorage energyStorage = tile.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite());
                energyStorage.receiveEnergy(10, false);
            }
        }
    }
}
