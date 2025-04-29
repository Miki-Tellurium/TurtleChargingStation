package com.mikitellurium.turtlechargingstation.common.blockentity;

import com.mikitellurium.telluriumforge.blockentity.TickingBlockEntity;
import com.mikitellurium.telluriumforge.capability.CapabilityHelper;
import com.mikitellurium.turtlechargingstation.common.block.ThunderchargeDynamoBlock;
import com.mikitellurium.turtlechargingstation.common.energy.NetworkNode;
import com.mikitellurium.turtlechargingstation.common.integration.computercraft.ThunderchargeDynamoPeripheral;
import com.mikitellurium.turtlechargingstation.registry.ModBlockEntities;
import dan200.computercraft.api.peripheral.PeripheralCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.energy.EmptyEnergyStorage;
import net.neoforged.neoforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;

public class ThunderchargeDynamoBlockEntity extends BlockEntity implements TickingBlockEntity {

    private int charge = 0;
    public static ModConfigSpec.IntValue TRANSFER_RATE;
    public static ModConfigSpec.IntValue RECHARGE_AMOUNT;

    public ThunderchargeDynamoBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.THUNDERCHARGE_DYNAMO.get(), blockPos, blockState);
    }

    @Override
    public void serverTick(ServerLevel level, BlockPos blockPos, BlockState blockState) {
        if (this.charge > 0) {

            BlockPos belowPos = blockPos.relative(Direction.DOWN);
            BlockEntity blockEntity = level.getBlockEntity(belowPos);
            if (blockEntity != null) {
                if (blockEntity instanceof NetworkNode node && node.hasNetwork()) {
                    node.getNetwork().getReceivers().forEach((receiver) -> {
                        CapabilityHelper.getOptional(level, Capabilities.EnergyStorage.BLOCK, receiver.getBlockPos(), receiver.getBlockState(), receiver, null)
                                .ifPresent((energyStorage) -> energyStorage.receiveEnergy(TRANSFER_RATE.get(), false));
                    });
                    node.getNetwork().getNodeByType(CopperCableBlockEntity.class).forEach(CopperCableBlockEntity::setBurning);
                } else {
                    CapabilityHelper.getOptional(level, Capabilities.EnergyStorage.BLOCK, belowPos, blockEntity.getBlockState(), blockEntity, Direction.DOWN)
                            .ifPresent((energyStorage) -> energyStorage.receiveEnergy(TRANSFER_RATE.get(), false));                }
            }

            this.charge--;
        }

        level.setBlockAndUpdate(blockPos, blockState.setValue(ThunderchargeDynamoBlock.POWERED, this.charge > 0));
        setChanged(level, blockPos, blockState);
    }

    public int getCharge() {
        return charge;
    }

    public void recharge() {
        this.charge = Math.min(this.getCharge() + RECHARGE_AMOUNT.get(), Integer.MAX_VALUE);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        charge = nbt.getInt("charge");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.putInt("charge", charge);
        super.saveAdditional(nbt, registries);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.THUNDERCHARGE_DYNAMO.get(), (blockEntity, side) -> EmptyEnergyStorage.INSTANCE);
        event.registerBlockEntity(PeripheralCapability.get(), ModBlockEntities.THUNDERCHARGE_DYNAMO.get(), (blockEntity, side) -> new ThunderchargeDynamoPeripheral(blockEntity));
    }

}
