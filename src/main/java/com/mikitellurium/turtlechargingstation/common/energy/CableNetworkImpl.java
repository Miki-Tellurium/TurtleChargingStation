package com.mikitellurium.turtlechargingstation.common.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CableNetworkImpl implements CableNetwork {

    private static int ids = 0; // Keep track of the networks created, for debug purposes
    private static int id() {
        return ++ids;
    }

    private final int id;
    private final Set<NetworkNode> nodes = new HashSet<>();
    private final Set<BlockEntity> receivers = new HashSet<>();

    public CableNetworkImpl(NetworkNode node) {
        this.nodes.add(node);
        this.id = id();
    }

    public int getId() {
        return id;
    }

    @Override
    public int size() {
        return nodes.size() + receivers.size();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void update(NetworkNode startingPoint) {
        if (!startingPoint.getLevel().isClientSide) {
            receivers.clear();
            Set<NetworkNode> nodes = this.findConnectedNodes(startingPoint.getLevel(), startingPoint.getBlockPos(), null, new HashSet<>());
            nodes.forEach((node) -> {
                this.addNode(node);
                this.findReceivers(node);
            });
        }
    }

    private Set<NetworkNode> findConnectedNodes(Level level, BlockPos startPos, Direction startDirection, Set<NetworkNode> foundNodes) {
        BlockEntity blockEntity = level.getBlockEntity(startPos);
        if (blockEntity instanceof NetworkNode node) {
            if (foundNodes.contains(node) || node.ignoreOnUpdate()) return foundNodes;
            foundNodes.add(node);

            for (Direction direction : Direction.values()) {
                if (startDirection != null && direction == startDirection.getOpposite()) continue;

                BlockPos relativePos = startPos.relative(direction);
                BlockEntity blockEntity1 = level.getBlockEntity(relativePos);
                if (blockEntity1 instanceof NetworkNode) {
                    findConnectedNodes(level, relativePos, direction, foundNodes);
                }
            }
        }

        return foundNodes;
    }

    private void findReceivers(NetworkNode node) {
        for (Direction direction : Direction.values()) {
            BlockPos relativePos = node.getBlockPos().relative(direction);
            BlockEntity blockEntity = node.getLevel().getBlockEntity(relativePos);
            if (blockEntity != null && !(blockEntity instanceof NetworkNode) && blockEntity.getCapability(ForgeCapabilities.ENERGY, direction).isPresent()) {
                this.addReceiver(blockEntity);
            }
        }
    }

    @Override
    public boolean addNode(NetworkNode node) {
        if (node != null) {
            node.setNetwork(this);
            return nodes.add(node);
        }
        return false;
    }

    @Override
    public void removeNode(NetworkNode node) {
        this.nodes.remove(node);
    }

    @Override
    public Collection<NetworkNode> getNodes() {
        return Collections.unmodifiableSet(nodes);
    }

    @Override
    public boolean addReceiver(BlockEntity blockEntity) {
        if (blockEntity != null && blockEntity.getCapability(ForgeCapabilities.ENERGY).isPresent()) {
            return receivers.add(blockEntity);
        }
        return false;
    }

    @Override
    public boolean removeReceiver(BlockEntity blockEntity) {
        return this.receivers.remove(blockEntity);
    }

    @Override
    public Collection<BlockEntity> getReceivers() {
        return Collections.unmodifiableSet(receivers);
    }

    @Override
    public boolean contains(BlockEntity blockEntity) {
        return blockEntity instanceof NetworkNode ? nodes.contains(blockEntity) : receivers.contains(blockEntity);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[Id:" + id + ", Nodes:" + nodes.size() + ", Receivers:" + receivers.size() + "]";
    }

}
