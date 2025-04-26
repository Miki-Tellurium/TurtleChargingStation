package com.mikitellurium.turtlechargingstation.networking.payloads;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class BlockEntitySyncPayload<T> implements CustomPacketPayload {

    public static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> makeId(ResourceLocation id) {
        return new Type<>(id);
    }

    private final BlockPos blockPos;
    private final T value;

    public BlockEntitySyncPayload(BlockPos blockPos, T value) {
        this.blockPos = blockPos;
        this.value = value;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public T getValue() {
        return value;
    }

    //public abstract void write(RegistryFriendlyByteBuf buf);

    public abstract void handle(IPayloadContext context);

    @Override
    public abstract Type<? extends CustomPacketPayload> type();

}
