package com.mikitellurium.turtlechargingstation.common.blockentity;

import com.mikitellurium.telluriumforge.blockentity.TickingBlockEntity;
import com.mikitellurium.telluriumforge.config.RangedConfigEntry;
import com.mikitellurium.turtlechargingstation.common.block.ThunderchargeDynamoBlock;
import com.mikitellurium.turtlechargingstation.common.energy.NetworkNode;
import com.mikitellurium.turtlechargingstation.common.integration.computercraft.ThunderchargeDynamoPeripheral;
import com.mikitellurium.turtlechargingstation.registry.ModBlockEntities;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import team.reborn.energy.api.EnergyStorage;

public class ThunderchargeDynamoBlockEntity extends BlockEntity implements TickingBlockEntity {

    private int charge = 0;
    public static RangedConfigEntry<Long> TRANSFER_RATE;
    public static RangedConfigEntry<Integer> RECHARGE_AMOUNT;

    public ThunderchargeDynamoBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.THUNDERCHARGE_DYNAMO, blockPos, blockState);
    }

    @Override
    public void serverTick(ServerWorld world, BlockPos blockPos, BlockState blockState) {
        if (this.charge > 0) {

            BlockPos belowPos = blockPos.offset(Direction.DOWN);
            BlockEntity blockEntity = world.getBlockEntity(belowPos);
            if (blockEntity != null) {
                if (blockEntity instanceof NetworkNode node && node.hasNetwork()) {
                    node.getNetwork().getReceivers().forEach((receiver) -> {
                        EnergyStorage energyStorage = EnergyStorage.SIDED.find(world, belowPos, receiver.getCachedState(), receiver, null);
                        if (energyStorage != null) {
                            try (Transaction transaction = Transaction.openOuter()) {
                                long amountInserted = energyStorage.insert(TRANSFER_RATE.get(), transaction);
                                if (amountInserted > 0) {
                                    transaction.commit();
                                }
                            }
                        }
                    });
                    node.getNetwork().getNodeByType(CopperCableBlockEntity.class).forEach(CopperCableBlockEntity::setBurning);
                } else {
                    EnergyStorage energyStorage = EnergyStorage.SIDED.find(world, belowPos, blockEntity.getCachedState(), blockEntity, Direction.UP);
                    if (energyStorage != null) {
                        try (Transaction transaction = Transaction.openOuter()) {
                            long amountInserted = energyStorage.insert(TRANSFER_RATE.get(), transaction);
                            if (amountInserted > 0) {
                                transaction.commit();
                            }
                        }
                    }
                }
            }

            this.charge--;
        }

        world.setBlockState(blockPos, blockState.with(ThunderchargeDynamoBlock.POWERED, this.charge > 0));
        markDirty(world, blockPos, blockState);
    }

    public int getCharge() {
        return charge;
    }

    public void recharge() {
        this.charge = Math.min(this.getCharge() + RECHARGE_AMOUNT.get(), Integer.MAX_VALUE);
    }

    public EnergyStorage energyLookup(Direction side) {
        return side == Direction.DOWN ? EnergyStorage.EMPTY : null;
    }

    public IPeripheral peripheralLookup(Direction side) {
        return new ThunderchargeDynamoPeripheral(this);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.charge = nbt.getInt("charge");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("charge", charge);
        super.writeNbt(nbt);
    }

}
