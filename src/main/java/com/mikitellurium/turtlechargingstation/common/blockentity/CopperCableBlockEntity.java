package com.mikitellurium.turtlechargingstation.common.blockentity;

import com.mikitellurium.telluriumforge.blockentity.TickingBlockEntity;
import com.mikitellurium.telluriumforge.networking.NetworkingHelper;
import com.mikitellurium.turtlechargingstation.common.block.CopperCableBlock;
import com.mikitellurium.turtlechargingstation.common.energy.CableNetwork;
import com.mikitellurium.turtlechargingstation.common.energy.CableNetworkImpl;
import com.mikitellurium.turtlechargingstation.common.energy.NetworkNode;
import com.mikitellurium.turtlechargingstation.networking.payloads.CableIdSyncPayload;
import com.mikitellurium.turtlechargingstation.registry.ModBlockEntities;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;

import java.util.*;

public class CopperCableBlockEntity extends BlockEntity implements TickingBlockEntity, NetworkNode {

    private CableNetwork cableNetwork;
    private boolean ignoreOnUpdate = false;
    private int burningTimer = 0;
    private boolean firstTick = true;

    public CopperCableBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.COPPER_CABLE, pos, blockState);
    }

    @Override
    public void tick(World world, BlockPos blockPos, BlockState blockState) {
        if (firstTick) {
            this.updateConnections();
            firstTick = false;
        }
        TickingBlockEntity.super.tick(world, blockPos, blockState);
    }

    @Override
    public void serverTick(ServerWorld level, BlockPos blockPos, BlockState blockState) {
        if (this.burningTimer > 0) {
            if (!blockState.get(CopperCableBlock.BURNING)) {
                level.setBlockState(blockPos, blockState.with(CopperCableBlock.BURNING, true));
            }
            this.burningTimer--;
        } else if (blockState.get(CopperCableBlock.BURNING)) {
            level.setBlockState(blockPos, blockState.with(CopperCableBlock.BURNING, false));
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void setBurning() {
        this.burningTimer = 200;
    }

    @SuppressWarnings("ConstantConditions")
    public void updateConnections() {
        if (this.world.isClient) return;

        Set<CopperCableBlockEntity> cableSet = this.getAdjacentCables();
        if (!cableSet.isEmpty()) {
            this.joinNetwork(cableSet);
        }
        if (!this.hasNetwork()) {
            this.setNetwork(new CableNetworkImpl(this));
        }
    }

    private void joinNetwork(Set<CopperCableBlockEntity> cableSet) {
        cableSet.stream()
                .filter(CopperCableBlockEntity::hasNetwork)
                .map(CopperCableBlockEntity::getNetwork)
                .max(Comparator.comparingInt(CableNetwork::size)) // Get the largest network
                .ifPresent(network -> network.update(this));
    }

    @SuppressWarnings("ConstantConditions")
    public void remove() {
        if (this.world.isClient) return;

        this.cableNetwork.removeNode(this);
        this.ignoreOnUpdate = true;
        Set<CopperCableBlockEntity> cableSet = this.getAdjacentCables();
        List<CableNetwork> cachedNetworks = new ArrayList<>(); // Cache network to avoid creating too many new ones
        cableSet.forEach((cable) -> {
            if (cable.hasNetwork() && !cachedNetworks.contains(cable.getNetwork())) {
                CableNetworkImpl newNetwork = new CableNetworkImpl(cable);
                cachedNetworks.add(newNetwork);
                cable.setNetwork(newNetwork);
                cable.getNetwork().update(cable);
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    public Set<CopperCableBlockEntity> getAdjacentCables() {
        Set<CopperCableBlockEntity> cableSet = new HashSet<>();
        for (Direction direction : Direction.values()) {
            BlockPos relativePos = this.pos.offset(direction);
            BlockEntity blockEntity = world.getBlockEntity(relativePos);
            if (blockEntity instanceof CopperCableBlockEntity cable && !cable.ignoreOnUpdate) {
                cableSet.add(cable);
            }
        }
        return cableSet;
    }

    @Override
    public boolean hasNetwork() {
        return this.cableNetwork != null;
    }

    @Override
    public CableNetwork getNetwork() {
        return this.cableNetwork;
    }

    @Override
    public void setNetwork(CableNetwork cableNetwork) {
        Objects.requireNonNull(cableNetwork);
        this.cableNetwork = cableNetwork;
        this.syncClientId(); // Only syncs if dev
    }

    @Override
    public boolean ignoreOnUpdate() {
        return ignoreOnUpdate;
    }

    @Override
    public World getNodeWorld() {
        return world;
    }

    @Override
    public BlockPos getBlockPos() {
        return pos;
    }

    public EnergyStorage energyLookup(Direction side) {
        return EnergyStorage.EMPTY;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup lookup) {
        NbtCompound nbt = super.toInitialChunkDataNbt(lookup);
        nbt.putInt("networkId", cableNetwork != null ? ((CableNetworkImpl)cableNetwork).getId() : clientNetworkId);
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.readNbt(nbt, lookup);
        burningTimer = nbt.getInt("burningTimer");
        if (nbt.contains("networkId")) {
            this.setClientNetworkId(nbt.getInt("networkId"));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        nbt.putInt("burningTimer", burningTimer);
        super.writeNbt(nbt, lookup);
    }
//
//    /*==DEBUG==*/
    private int clientNetworkId = -1;

    public void setClientNetworkId(int id) {
        this.clientNetworkId = id;
    }

    @SuppressWarnings("ConstantConditions")
    public int getClientNetworkId() {
        return this.world.isClient ? this.clientNetworkId : -2;
    }

    @SuppressWarnings("ConstantConditions")
    private void syncClientId() {
        if (FabricLoader.getInstance().isDevelopmentEnvironment() && !this.world.isClient) {
            NetworkingHelper.sendToTrackingClients((ServerWorld) this.world, this.pos, new CableIdSyncPayload(this.pos, ((CableNetworkImpl) cableNetwork).getId()));
        }
    }
    /**/
}
