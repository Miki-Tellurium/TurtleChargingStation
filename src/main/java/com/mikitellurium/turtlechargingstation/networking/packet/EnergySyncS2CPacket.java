package com.mikitellurium.turtlechargingstation.networking.packet;

import com.mikitellurium.telluriumforge.networking.packet.IntSyncPacket;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationContainer;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;

public class EnergySyncS2CPacket extends IntSyncPacket {
    public EnergySyncS2CPacket() {
        super();
    }

    public EnergySyncS2CPacket(BlockPos pos, Integer value) {
        super(pos, value);
    }

    public static IMessageHandler<EnergySyncS2CPacket, IMessage> handler() {
        return (message, context) -> {
            TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(message.getBlockPos());
            if(tile instanceof TurtleChargingStationTileEntity) {
                TurtleChargingStationTileEntity station = (TurtleChargingStationTileEntity) tile;
                station.setEnergy(message.getValue());

                Container container = Minecraft.getMinecraft().player.openContainer;
                if(container instanceof TurtleChargingStationContainer && ((TurtleChargingStationContainer)container).getTile().getPos().equals(message.getBlockPos())) {
                    station.setEnergy(message.getValue());
                }
            }
            return null;
        };
    }
}
