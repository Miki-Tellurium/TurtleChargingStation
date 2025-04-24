package com.mikitellurium.turtlecharginstation.common.blockentity;

import com.mikitellurium.telluriumforge.blockentity.TickingBlockEntity;
import com.mikitellurium.turtlecharginstation.common.block.ThunderchargeDynamoBlock;
import com.mikitellurium.turtlecharginstation.common.energy.NetworkNode;
import com.mikitellurium.turtlecharginstation.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EmptyEnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ThunderchargeDynamoBlockEntity extends BlockEntity implements TickingBlockEntity {

    private final LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.of(() -> EmptyEnergyStorage.INSTANCE);
    private int charge = 0;
    public static ForgeConfigSpec.IntValue TRANSFER_RATE;
    public static ForgeConfigSpec.IntValue RECHARGE_AMOUNT;

    public ThunderchargeDynamoBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.THUNDERCHARGE_DYNAMO.get(), blockPos, blockState);
    }

    @Override
    public void serverTick(ServerLevel level, BlockPos blockPos, BlockState blockState) {
        if (this.charge > 0) {

            BlockEntity blockEntity = level.getBlockEntity(blockPos.relative(Direction.DOWN));
            if (blockEntity != null) {
                if (blockEntity instanceof NetworkNode node && node.hasNetwork()) {
                    node.getNetwork().getReceivers().forEach((receiver) -> {
                        receiver.getCapability(ForgeCapabilities.ENERGY, Direction.DOWN).ifPresent((energyStorage) -> energyStorage.receiveEnergy(TRANSFER_RATE.get(), false));
                    });
                    node.getNetwork().getNodeByType(CopperCableBlockEntity.class).forEach(CopperCableBlockEntity::setBurning);
                } else {
                    blockEntity.getCapability(ForgeCapabilities.ENERGY, Direction.DOWN).ifPresent((energyStorage) -> energyStorage.receiveEnergy(TRANSFER_RATE.get(), false));
                }
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
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY && side == Direction.DOWN) {
            return lazyEnergyHandler.cast();
        }
        return super.getCapability(cap, side);
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

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyHandler.invalidate();
    }

}
