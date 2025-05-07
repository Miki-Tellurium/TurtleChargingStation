package com.mikitellurium.turtlechargingstation.networking.payloads;

import com.mikitellurium.telluriumforge.networking.payload.BlockEntitySyncPayload;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationScreenHandler;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlechargingstation.util.FastId;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public class EnergySyncPayload extends BlockEntitySyncPayload<Long> {
    public static final Id<EnergySyncPayload> ID = new Id<>(FastId.ofMod("energy_sync"));
    public static final PacketCodec<PacketByteBuf, EnergySyncPayload> CODEC = getCodec(PacketCodecs.VAR_LONG, EnergySyncPayload::new);

    public EnergySyncPayload(BlockPos blockPos, long energy) {
        super(blockPos, energy);
    }

    @Override
    public void handle(ClientPlayNetworking.Context context) {
        ClientPlayerEntity player = context.player();
        if (player.currentScreenHandler instanceof TurtleChargingStationScreenHandler screen &&
                screen.getBlockEntity().getPos().equals(this.getBlockPos()) &&
                player.getWorld().getBlockEntity(this.getBlockPos()) instanceof TurtleChargingStationBlockEntity station) {
            station.setAmount(this.getValue());
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
