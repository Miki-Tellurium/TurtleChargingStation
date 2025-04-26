package com.mikitellurium.turtlechargingstation.networking.payloads;

import net.minecraft.core.BlockPos;

public abstract class IntSyncPayload extends BlockEntitySyncPayload<Integer> {

    public IntSyncPayload(BlockPos blockPos, Integer value) {
        super(blockPos, value);
    }

}
