package com.mikitellurium.turtlechargingstation.common.energy;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtLong;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public abstract class ModEnergyStorage extends SimpleEnergyStorage {

    public ModEnergyStorage(long capacity, long maxInsert) {
        this(capacity, maxInsert, maxInsert);
    }

    public ModEnergyStorage(long capacity, long maxInsert, long maxExtract) {
        super(capacity, maxInsert, maxExtract);
    }

    @Override
    protected abstract void onFinalCommit();

    public long insert(long amount, boolean simulate) {
        try (Transaction transaction = Transaction.openOuter()) {
            long amountInserted = super.insert(amount, transaction);
            if (amountInserted > 0) {
                if (!simulate) {
                    transaction.commit();
                }
                return amountInserted;
            }
        }
        return 0;
    }

    public long extract(long amount, boolean simulate) {
        try (Transaction transaction = Transaction.openOuter()) {
            long amountExtracted = super.extract(amount, transaction);
            if (amountExtracted > 0) {
                if (!simulate) {
                    transaction.commit();
                }
                return amountExtracted;
            }
        }
        return 0;
    }

    public void setAmount(long energy) {
        this.amount = Math.max(0, Math.min(energy, this.getCapacity()));
    }

    public NbtElement writeNbt() {
        return NbtLong.of(this.getAmount());
    }

    public void readNBT(NbtElement nbt) {
        if (!(nbt instanceof NbtLong nbtLong))
            throw new IllegalArgumentException("Cannot read an Nbt that isn't a long value");
        this.setAmount(nbtLong.longValue());
    }

}
