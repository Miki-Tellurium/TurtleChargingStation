package com.mikitellurium.turtlechargingstation.common.energy;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface NetworkNode {
    CableNetwork getNetwork();

    void setNetwork(CableNetwork cableNetwork);

    boolean hasNetwork();

    boolean ignoreOnUpdate();

    World getNodeWorld();

    BlockPos getBlockPos();
}
