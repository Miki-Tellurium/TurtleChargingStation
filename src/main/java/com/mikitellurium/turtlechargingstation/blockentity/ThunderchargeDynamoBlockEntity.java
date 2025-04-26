package com.mikitellurium.turtlechargingstation.blockentity;

import com.mikitellurium.turtlechargingstation.block.ThunderchargeDynamoBlock;
import com.mikitellurium.turtlechargingstation.registry.ModBlockEntities;
import com.mikitellurium.turtlechargingstation.registry.ModTags;
import com.mikitellurium.turtlechargingstation.util.ConductiveBlockContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ThunderchargeDynamoBlockEntity extends BlockEntity {

    private int charge = 0;
    public static ModConfigSpec.IntValue TRANSFER_RATE;
    public static ModConfigSpec.IntValue RECHARGE_AMOUNT;

    public ThunderchargeDynamoBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.THUNDERCHARGE_DYNAMO.get(), blockPos, blockState);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (level.isClientSide) {
            return;
        }

        if (this.charge > 0) {

            Set<BlockEntity> chargedBlockEntities = new HashSet<>(); // Avoid block entities being charged multiple times per tick
            for (Direction direction : Direction.values()) {
                if (direction == Direction.UP) {
                    continue;
                }
                Map<BlockEntity, Direction> blockEntities = this.findConnectedBlockEntities(level, blockPos, direction, new HashSet<>());
                if (blockEntities.isEmpty()) {
                    continue;
                }
                blockEntities.entrySet().stream()
                        .filter((entry) -> !chargedBlockEntities.contains(entry.getKey()))
                        .forEach((entry) -> {
                            BlockEntity blockEntity = entry.getKey();
                            IEnergyStorage energy = level.getCapability(Capabilities.EnergyStorage.BLOCK, blockEntity.getBlockPos(),
                                    blockEntity.getBlockState(), blockEntity, direction);
                            if (energy != null) {
                                energy.receiveEnergy(TRANSFER_RATE.get(), false);
                                chargedBlockEntities.add(blockEntity);
                            }
                        });
            }

            this.charge--;
        }

        level.setBlockAndUpdate(blockPos, blockState.setValue(ThunderchargeDynamoBlock.POWERED, this.charge > 0));
        setChanged(level, blockPos, blockState);
    }

    /*
     * checkedPosSet: tracks which BlockPos are already checked to avoid loops.
     * If a BlockEntity is found add it to blockEntities, else if a conductive block is found
     * check adjacent blocks using recursion.
     */
    private Map<BlockEntity, Direction> findConnectedBlockEntities(Level level, BlockPos startPos, Direction startDirection,
                                                                   Set<BlockPos> checkedPosSet) {
        Map<BlockEntity, Direction> blockEntities = new HashMap<>();
        BlockPos relative = startPos.relative(startDirection);
        if (checkedPosSet.contains(relative)) return blockEntities; // Don't check the same BlockPos more than once

        BlockState blockState = level.getBlockState(relative);
        ConductiveBlockContext context = new ConductiveBlockContext(blockState);

        BlockEntity blockEntity = level.getBlockEntity(relative);
        if (blockEntity != null) {
            blockEntities.put(blockEntity, startDirection);
        } else if (blockState.is(ModTags.DYNAMO_CONDUCTIVE_BLOCKS)) {
            // Check if the previous block is oriented towards the current position
            if (!context.canConductTo(level.getBlockState(startPos), startDirection)) return blockEntities;

            checkedPosSet.add(relative);
            for (Direction direction : context.getDirections()) {
                if (direction == startDirection.getOpposite()) continue; // Don't check the direction we're coming from
                blockEntities.putAll(findConnectedBlockEntities(level, relative, direction, checkedPosSet));
            }
        }
        return blockEntities;
    }

    public int getCharge() {
        return this.charge;
    }

    public void recharge() {
        this.charge = Math.min(this.getCharge() + RECHARGE_AMOUNT.get(), Integer.MAX_VALUE);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        charge = tag.getInt("dynamoCharge");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("dynamoCharge", charge);
        super.saveAdditional(tag, provider);
    }

    public static void registerCapability(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.THUNDERCHARGE_DYNAMO.get(),
                (blockEntity, direction) -> {
                    if (direction != Direction.UP) {
                        return new EnergyStorage(0);
                    }
                    return null;
                });
    }

}
