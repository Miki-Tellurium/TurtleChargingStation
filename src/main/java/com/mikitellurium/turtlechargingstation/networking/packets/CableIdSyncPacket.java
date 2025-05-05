package com.mikitellurium.turtlechargingstation.networking.packets;

import com.mikitellurium.telluriumforge.networking.packet.IntSyncPacket;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationScreenHandler;
import com.mikitellurium.turtlechargingstation.common.blockentity.CopperCableBlockEntity;
import com.mikitellurium.turtlechargingstation.util.FastId;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class CableIdSyncPacket extends IntSyncPacket {
    public static final PacketType<CableIdSyncPacket> TYPE = PacketType.create(FastId.ofMod("id_sync_payload"), CableIdSyncPacket::new);

    public CableIdSyncPacket(BlockPos blockPos, int id) {
        super(blockPos, id);
    }

    public CableIdSyncPacket(PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public void handle(ClientPlayerEntity player, PacketSender responseSender) {
        if(player.getWorld().getBlockEntity(this.getBlockPos()) instanceof CopperCableBlockEntity blockEntity) {
            blockEntity.setClientNetworkId(this.getValue());
        }
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
