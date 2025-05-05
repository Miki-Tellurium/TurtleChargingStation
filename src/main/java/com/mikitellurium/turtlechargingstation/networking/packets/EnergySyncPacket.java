package com.mikitellurium.turtlechargingstation.networking.packets;

import com.mikitellurium.telluriumforge.networking.packet.LongSyncPacket;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationScreenHandler;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlechargingstation.util.FastId;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class EnergySyncPacket extends LongSyncPacket {
    public static final PacketType<EnergySyncPacket> TYPE = PacketType.create(FastId.ofMod("energy_sync"), EnergySyncPacket::new);

    public EnergySyncPacket(BlockPos blockPos, long energy) {
        super(blockPos, energy);
    }

    public EnergySyncPacket(PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public void handle(ClientPlayerEntity player, PacketSender responseSender) {
        if (player.currentScreenHandler instanceof TurtleChargingStationScreenHandler screen &&
                screen.getBlockEntity().getPos().equals(this.getBlockPos()) &&
                player.getWorld().getBlockEntity(this.getBlockPos()) instanceof TurtleChargingStationBlockEntity station) {
            station.setAmount(this.getValue());
        }
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
