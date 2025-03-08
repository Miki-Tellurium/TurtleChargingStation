package com.mikitellurium.turtlecharginstation.blockentity;

import com.mikitellurium.telluriumforge.blockentity.TickingBlockEntity;
import com.mikitellurium.turtlecharginstation.block.ThunderchargeDynamoBlock;
import com.mikitellurium.turtlecharginstation.registry.ModBlockEntities;
import com.mikitellurium.turtlecharginstation.registry.ModTags;
import com.mikitellurium.turtlecharginstation.util.ConductiveBlockContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ThunderchargeDynamoBlockEntity extends BlockEntity implements TickingBlockEntity {

    private int charge = 0;
    public static ForgeConfigSpec.IntValue TRANSFER_RATE;
    public static ForgeConfigSpec.IntValue RECHARGE_AMOUNT;

    public ThunderchargeDynamoBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.THUNDERCHARGE_DYNAMO.get(), blockPos, blockState);
    }

    @Override
    public void serverTick(ServerLevel level, BlockPos blockPos, BlockState blockState) {
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
                            BlockEntity be = entry.getKey();
                            if (be.getCapability(ForgeCapabilities.ENERGY).isPresent()) {
                                be.getCapability(ForgeCapabilities.ENERGY).ifPresent((energyStorage) -> energyStorage.receiveEnergy(TRANSFER_RATE.get(), false));
                                chargedBlockEntities.add(be);
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
        return charge;
    }

    public void recharge() {
        this.charge = Math.min(this.getCharge() + RECHARGE_AMOUNT.get(), Integer.MAX_VALUE);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        charge = nbt.getInt("charge");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt("charge", charge);
        super.saveAdditional(nbt);
    }

}
