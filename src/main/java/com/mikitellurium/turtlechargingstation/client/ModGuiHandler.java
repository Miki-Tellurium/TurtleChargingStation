package com.mikitellurium.turtlechargingstation.client;

import com.mikitellurium.turtlechargingstation.TurtleChargingStationMod;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationContainer;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationGui;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class ModGuiHandler implements IGuiHandler {
    public static final ModGuiHandler INSTANCE = new ModGuiHandler();

    private ModGuiHandler() {}

    public static void openTurtleChargingStationGui(EntityPlayer player, TurtleChargingStationTileEntity station) {
        BlockPos pos = station.getPos();
        player.openGui(TurtleChargingStationMod.getInstance(), 0, player.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ());
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (id == 0) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TurtleChargingStationTileEntity) {
                return new TurtleChargingStationContainer(player.inventory, (TurtleChargingStationTileEntity)tile);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (id == 0) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TurtleChargingStationTileEntity) {
                return new TurtleChargingStationGui(player.inventory, (TurtleChargingStationTileEntity)tile);
            }
        }
        return null;
    }
}
