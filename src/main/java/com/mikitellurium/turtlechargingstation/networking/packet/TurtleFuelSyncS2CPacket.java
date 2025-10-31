package com.mikitellurium.turtlechargingstation.networking.packet;

import com.mikitellurium.telluriumforge.networking.packet.IntSyncPacket;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationContainer;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;

public class TurtleFuelSyncS2CPacket extends IntSyncPacket {
    public TurtleFuelSyncS2CPacket() {
        super();
    }

    public TurtleFuelSyncS2CPacket(BlockPos pos, Integer value) {
        super(pos, value);
    }

    public static IMessageHandler<TurtleFuelSyncS2CPacket, IMessage> handler() {
        return (message, context) -> {
            TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(message.getBlockPos());
            if (tile instanceof TileTurtle) {
                TileTurtle turtle = (TileTurtle) tile;
                turtle.getAccess().setFuelLevel(message.getValue());

                Container container = Minecraft.getMinecraft().player.openContainer;
                if(container instanceof TurtleChargingStationContainer && ((TurtleChargingStationContainer)container).getTile().getPos().equals(message.getBlockPos())) {
                    turtle.getAccess().setFuelLevel(message.getValue());
                }
            }
            return null;
        };
    }
}
