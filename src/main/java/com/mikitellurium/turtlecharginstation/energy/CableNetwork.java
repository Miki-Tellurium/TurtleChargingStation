package com.mikitellurium.turtlecharginstation.energy;

import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collection;

public interface CableNetwork {

    void update(NetworkNode startingPoint);

    boolean addNode(NetworkNode node);

    void removeNode(NetworkNode node);

    Collection<NetworkNode> getNodes();

    boolean addReceiver(BlockEntity blockEntity);

    boolean removeReceiver(BlockEntity blockEntity);

    Collection<BlockEntity> getReceivers();

    int size();

    boolean contains(BlockEntity blockEntity);

}
