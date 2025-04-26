package com.mikitellurium.turtlechargingstation.util;

import net.neoforged.neoforge.energy.EnergyStorage;

import java.util.function.Consumer;

public abstract class ModEnergyStorage extends EnergyStorage {

    private final Consumer<EnergyStorage> onEnergyChanged;

    public ModEnergyStorage(int capacity, int maxReceive) {
        this(capacity, maxReceive, (energyStorage) -> {});
    }

    public ModEnergyStorage(int capacity, int maxReceive, Consumer<EnergyStorage> onEnergyChanged) {
        super(capacity, maxReceive);
        this.onEnergyChanged = onEnergyChanged;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int receivedEnergy = super.receiveEnergy(maxReceive, simulate);
        if (receivedEnergy != 0) {
            onEnergyChanged();
        }

        return receivedEnergy;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extractedEnergy = super.extractEnergy(maxExtract, simulate);
        if (extractedEnergy != 0) {
            onEnergyChanged();
        }

        return extractedEnergy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void onEnergyChanged() {
        this.onEnergyChanged.accept(this);
    }

}
