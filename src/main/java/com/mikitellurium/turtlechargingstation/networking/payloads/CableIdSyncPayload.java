package com.mikitellurium.turtlechargingstation.networking.payloads;

import com.mikitellurium.telluriumforge.networking.payload.BlockEntitySyncPayload;
import com.mikitellurium.turtlechargingstation.common.blockentity.CopperCableBlockEntity;
import com.mikitellurium.turtlechargingstation.util.FastId;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public class CableIdSyncPayload extends BlockEntitySyncPayload<Integer> {
    public static final Id<CableIdSyncPayload> ID = new Id<>(FastId.ofMod("id_sync_payload"));
    public static final PacketCodec<PacketByteBuf, CableIdSyncPayload> CODEC = getCodec(PacketCodecs.INTEGER, CableIdSyncPayload::new);

    public CableIdSyncPayload(BlockPos blockPos, int id) {
        super(blockPos, id);
    }

    @Override
    public void handle(ClientPlayNetworking.Context context) {
        if(context.player().getWorld().getBlockEntity(this.getBlockPos()) instanceof CopperCableBlockEntity blockEntity) {
            blockEntity.setClientNetworkId(this.getValue());
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
