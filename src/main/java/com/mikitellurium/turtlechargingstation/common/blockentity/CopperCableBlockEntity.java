package com.mikitellurium.turtlechargingstation.common.blockentity;

import com.mikitellurium.telluriumforge.blockentity.TickingBlockEntity;
import com.mikitellurium.turtlechargingstation.common.block.CopperCableBlock;
import com.mikitellurium.turtlechargingstation.common.energy.CableNetwork;
import com.mikitellurium.turtlechargingstation.common.energy.CableNetworkImpl;
import com.mikitellurium.turtlechargingstation.common.energy.NetworkNode;
import com.mikitellurium.turtlechargingstation.networking.payloads.CableIdSyncPayload;
import com.mikitellurium.turtlechargingstation.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.EmptyEnergyStorage;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CopperCableBlockEntity extends BlockEntity implements TickingBlockEntity, NetworkNode {

    private CableNetwork cableNetwork;
    private boolean ignoreOnUpdate = false;
    private int burningTimer = 0;

    public CopperCableBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.COPPER_CABLE.get(), pos, blockState);
    }

    @Override
    public void serverTick(ServerLevel level, BlockPos blockPos, BlockState blockState) {
        if (this.burningTimer > 0) {
            if (!blockState.getValue(CopperCableBlock.BURNING)) {
                level.setBlockAndUpdate(blockPos, blockState.setValue(CopperCableBlock.BURNING, true));
            }
            this.burningTimer--;
        } else if (blockState.getValue(CopperCableBlock.BURNING)) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(CopperCableBlock.BURNING, false));
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void setBurning() {
        this.burningTimer = 200;
    }

    @SuppressWarnings("ConstantConditions")
    public void updateConnections() {
        if (this.level.isClientSide) return;

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
        if (this.level.isClientSide) return;

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
            BlockPos relativePos = this.worldPosition.relative(direction);
            BlockEntity blockEntity = level.getBlockEntity(relativePos);
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
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.putInt("networkId", Optional.of(((CableNetworkImpl)cableNetwork).getId()).orElse(clientNetworkId));
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains("networkId")) {
            this.setClientNetworkId(tag.getInt("networkId"));
        }
        super.handleUpdateTag(tag, registries);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onLoad() {
        super.onLoad();
        if (!this.level.isClientSide) {
            this.updateConnections();
        }
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        burningTimer = nbt.getInt("burningTimer");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.putInt("burningTimer", burningTimer);
        super.saveAdditional(nbt, registries);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.COPPER_CABLE.get(), (blockEntity, side) -> EmptyEnergyStorage.INSTANCE);
    }

    /*==DEBUG==*/
    private int clientNetworkId = -1;

    public void setClientNetworkId(int id) {
        this.clientNetworkId = id;
    }

    @SuppressWarnings("ConstantConditions")
    public int getClientNetworkId() {
        return this.level.isClientSide ? this.clientNetworkId : -2;
    }

    @SuppressWarnings("ConstantConditions")
    private void syncClientId() {
        if (!FMLLoader.isProduction() && !this.level.isClientSide) {
            ChunkPos chunkPos = this.level.getChunkAt(this.worldPosition).getPos();
            PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) this.level, chunkPos, new CableIdSyncPayload(this.worldPosition, ((CableNetworkImpl) cableNetwork).getId()));
        }
    }
    /**/

}
