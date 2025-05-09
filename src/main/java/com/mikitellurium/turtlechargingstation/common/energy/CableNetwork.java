package com.mikitellurium.turtlechargingstation.common.energy;

import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collection;
import java.util.stream.Collectors;

public interface CableNetwork {

    void update(NetworkNode startingPoint);

    boolean addNode(NetworkNode node);

    void removeNode(NetworkNode node);

    Collection<NetworkNode> getNodes();

    default <T extends NetworkNode> Collection<T> getNodeByType(Class<T> clazz) {
        return this.getNodes().stream().filter(clazz::isInstance).map(clazz::cast).collect(Collectors.toUnmodifiableSet());
    }

    boolean addReceiver(BlockEntity blockEntity);

    boolean removeReceiver(BlockEntity blockEntity);

    Collection<BlockEntity> getReceivers();

    int size();

    boolean contains(BlockEntity blockEntity);

}
