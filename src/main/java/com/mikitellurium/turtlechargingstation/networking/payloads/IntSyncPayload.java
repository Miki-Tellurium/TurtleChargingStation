package com.mikitellurium.turtlechargingstation.networking.payloads;

import com.mikitellurium.telluriumforge.networking.BlockEntitySyncPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.BiFunction;

public abstract class IntSyncPayload extends BlockEntitySyncPayload<Integer> {

    public static <P extends IntSyncPayload> StreamCodec<FriendlyByteBuf, P> getCodec(BiFunction<BlockPos, Integer, P> factory) {
        return BlockEntitySyncPayload.getCodec(ByteBufCodecs.INT, factory);
    }

    public IntSyncPayload(BlockPos blockPos, Integer value) {
        super(blockPos, value);
    }

}
