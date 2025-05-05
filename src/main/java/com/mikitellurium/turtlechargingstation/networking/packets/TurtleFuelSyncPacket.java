package com.mikitellurium.turtlechargingstation.networking.packets;

import com.mikitellurium.telluriumforge.networking.packet.IntSyncPacket;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationScreenHandler;
import com.mikitellurium.turtlechargingstation.util.FastId;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class TurtleFuelSyncPacket extends IntSyncPacket {
    public static final PacketType<TurtleFuelSyncPacket> TYPE = PacketType.create(FastId.ofMod("turtle_fuel_sync"), TurtleFuelSyncPacket::new);

    public TurtleFuelSyncPacket(BlockPos blockPos, int energy) {
        super(blockPos, energy);
    }

    public TurtleFuelSyncPacket(PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public void handle(ClientPlayerEntity player, PacketSender responseSender) {
        if (player.currentScreenHandler instanceof TurtleChargingStationScreenHandler && player.getWorld().getBlockEntity(this.getBlockPos()) instanceof TurtleBlockEntity turtle) {
            turtle.getAccess().setFuelLevel(this.getValue());
        }
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
