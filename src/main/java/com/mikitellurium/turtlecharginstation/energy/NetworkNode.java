package com.mikitellurium.turtlecharginstation.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface NetworkNode {

    CableNetwork getNetwork();

    void setNetwork(CableNetwork cableNetwork);

    boolean hasNetwork();

    boolean ignoreOnUpdate();

    Level getLevel();

    BlockPos getBlockPos();

}
