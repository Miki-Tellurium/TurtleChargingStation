package com.mikitellurium.turtlechargingstation.networking.payloads;

import com.mikitellurium.turtlechargingstation.common.blockentity.CopperCableBlockEntity;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class CableIdSyncPayload extends IntSyncPayload {

    public static final CustomPacketPayload.Type<CableIdSyncPayload> TYPE = new Type<>(FastLoc.modLoc("id_sync_payload"));
    public static final StreamCodec<FriendlyByteBuf, CableIdSyncPayload> CODEC = IntSyncPayload.getCodec(CableIdSyncPayload::new);

    public CableIdSyncPayload(BlockPos blockPos, Integer value) {
        super(blockPos, value);
    }

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(this.getBlockPos()) instanceof CopperCableBlockEntity blockEntity) {
                blockEntity.setClientNetworkId(this.getValue());
            }
        });
    }

    @NotNull
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
