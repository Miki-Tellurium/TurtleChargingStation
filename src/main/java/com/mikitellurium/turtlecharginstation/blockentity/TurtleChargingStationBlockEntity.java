package com.mikitellurium.turtlecharginstation.blockentity;

import com.mikitellurium.telluriumforge.blockentity.NameableBlockEntity;
import com.mikitellurium.telluriumforge.blockentity.TickingBlockEntity;
import com.mikitellurium.telluriumforge.energy.SimpleEnergyStorage;
import com.mikitellurium.turtlecharginstation.block.TurtleChargingStationBlock;
import com.mikitellurium.turtlecharginstation.gui.TurtleChargingStationMenu;
import com.mikitellurium.turtlecharginstation.networking.ModMessages;
import com.mikitellurium.turtlecharginstation.networking.packets.EnergySyncS2CPacket;
import com.mikitellurium.turtlecharginstation.networking.packets.TurtleFuelSyncS2CPacket;
import com.mikitellurium.turtlecharginstation.registry.ModBlockEntities;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TurtleChargingStationBlockEntity extends NameableBlockEntity implements TickingBlockEntity, MenuProvider {

    public static ForgeConfigSpec.IntValue CAPACITY;
    public static ForgeConfigSpec.IntValue CONVERSION_RATE; // Based on Thermal Expansion stirling dynamo production rate using coal
    private final int maxReceive = CONVERSION_RATE.get() * 6; // 6 sides
    private final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(CAPACITY.get(), maxReceive) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            ModMessages.sendToClients(new EnergySyncS2CPacket(this.energy, getBlockPos()));
        }
    };
    private final LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.of(() -> energyStorage);

    public TurtleChargingStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.TURTLE_CHARGING_STATION.get(), pPos, pBlockState);
    }

    @Override
    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        List<TurtleBlockEntity> turtles = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            BlockEntity be = level.getBlockEntity(this.worldPosition.relative(direction));
            if (be == null) continue;
            if (be instanceof TurtleBlockEntity) {
                turtles.add((TurtleBlockEntity) be);
            }
        }
        // State stays charging even if disabled
        boolean shouldCharge = !turtles.isEmpty() && this.hasChargeableTurtle(turtles) && this.energyStorage.getEnergyStored() >= CONVERSION_RATE.get();
        level.setBlock(pos, state.setValue(TurtleChargingStationBlock.CHARGING, shouldCharge), 2);
        if (shouldCharge && this.getBlockState().getValue(TurtleChargingStationBlock.ENABLED)) {
            for (TurtleBlockEntity turtle: turtles) {
                if (this.isChargeable(turtle) && this.energyStorage.getEnergyStored() >= CONVERSION_RATE.get()) {
                    this.refuelTurtle(turtle);
                }
            }
        }
    }

    private boolean hasChargeableTurtle(List<TurtleBlockEntity> turtles) {
        for (TurtleBlockEntity turtle: turtles) {
           if (this.isChargeable(turtle)) {
               return true;
            }
        }
        return false;
    }

    private boolean isChargeable(TurtleBlockEntity turtle) {
        return turtle.getAccess().getFuelLevel() < turtle.getAccess().getFuelLimit();
    }

    private void refuelTurtle(TurtleBlockEntity turtle) {
        turtle.getAccess().addFuel(1);
        this.energyStorage.extractEnergy(CONVERSION_RATE.get(), false);
        ModMessages.sendToClients(new TurtleFuelSyncS2CPacket(turtle.getAccess().getFuelLevel(), turtle.getBlockPos()));
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public int getEnergy() {
        return this.energyStorage.getEnergyStored();
    }

    public int getMaxEnergy() {
        return this.energyStorage.getMaxEnergyStored();
    }

    public void setEnergy(int energy) {
        this.energyStorage.setEnergy(energy);
    }

    // Gui
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new TurtleChargingStationMenu(id, inventory, this);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.turtlechargingstation.turtle_charging_station");
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    // Capabilities
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyHandler.invalidate();
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        energyStorage.setEnergy(nbt.getInt("turtle_charger.energy"));
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt("turtle_charger.energy", energyStorage.getEnergyStored());
        super.saveAdditional(nbt);
    }

}
