package com.mikitellurium.turtlecharginstation.blockentity;

import com.mikitellurium.telluriumforge.blockentity.TickingBlockEntity;
import com.mikitellurium.turtlecharginstation.energy.CableNetwork;
import com.mikitellurium.turtlecharginstation.energy.CableNetworkImpl;
import com.mikitellurium.turtlecharginstation.energy.NetworkNode;
import com.mikitellurium.turtlecharginstation.networking.ModMessages;
import com.mikitellurium.turtlecharginstation.networking.packets.CableIdSyncS2CPacket;
import com.mikitellurium.turtlecharginstation.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class CopperCableBlockEntity extends BlockEntity implements TickingBlockEntity, NetworkNode {

    private CableNetwork cableNetwork;
    private boolean ignoreOnUpdate = false;
    private int clientNetworkId = -1;

    public CopperCableBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.COPPER_CABLE.get(), pos, blockState);
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
        Optional<CableNetwork> optionalNetwork = cableSet.stream()
                .filter(CopperCableBlockEntity::hasNetwork)
                .map(CopperCableBlockEntity::getNetwork)
                .max(Comparator.comparingInt(CableNetwork::size)); // Get the largest network
        optionalNetwork.ifPresent(network -> {
            network.update(this);
        });
    }

    @SuppressWarnings("ConstantConditions")
    public void remove() {
        if (this.level.isClientSide) return;

        this.cableNetwork.removeNode(this);
        this.ignoreOnUpdate = true;
        Set<CopperCableBlockEntity> cableSet = this.getAdjacentCables();
        List<CableNetworkImpl> cachedNetworks = new ArrayList<>(); // Cache network to avoid creating too many new ones
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
            BlockEntity blockEntity = level.getExistingBlockEntity(relativePos);
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
        ModMessages.sendToClients(new CableIdSyncS2CPacket(((CableNetworkImpl)cableNetwork).getId(), this.worldPosition));
    }

    @Override
    public boolean ignoreOnUpdate() {
        return ignoreOnUpdate;
    }

    public void setClientNetworkId(int id) {
        this.clientNetworkId = id;
    }

    @SuppressWarnings("ConstantConditions")
    public int getClientNetworkId() {
        return this.level.isClientSide ? this.clientNetworkId : -2;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putInt("networkId", ((CableNetworkImpl)cableNetwork).getId());
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if (tag.contains("networkId")) {
            this.setClientNetworkId(tag.getInt("networkId"));
        }
        super.handleUpdateTag(tag);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onLoad() {
        super.onLoad();
        if (!this.level.isClientSide) {
            this.updateConnections();
        }
    }

}
