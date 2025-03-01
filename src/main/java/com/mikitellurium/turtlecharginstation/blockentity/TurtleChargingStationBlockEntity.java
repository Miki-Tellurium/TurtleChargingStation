package com.mikitellurium.turtlecharginstation.blockentity;

import com.mikitellurium.telluriumforge.blockentity.TickingBlockEntity;
import com.mikitellurium.telluriumforge.energy.SimpleEnergyStorage;
import com.mikitellurium.turtlecharginstation.block.TurtleChargingStationBlock;
import com.mikitellurium.turtlecharginstation.gui.TurtleChargingStationMenu;
import com.mikitellurium.turtlecharginstation.networking.ModMessages;
import com.mikitellurium.turtlecharginstation.networking.packets.EnergySyncS2CPacket;
import com.mikitellurium.turtlecharginstation.networking.packets.TurtleFuelSyncS2CPacket;
import com.mikitellurium.turtlecharginstation.registry.ModBlockEntities;
import dan200.computercraft.shared.ModRegistry;
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
import net.minecraft.world.level.Level;
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

public class TurtleChargingStationBlockEntity extends BlockEntity implements TickingBlockEntity, MenuProvider {

    public static ForgeConfigSpec.IntValue CAPACITY;
    public static ForgeConfigSpec.IntValue CONVERSION_RATE; // Based on Thermal Expansion stirling dynamo production rate using coal
    private final int maxReceive = CONVERSION_RATE.get() * 6; // 6 sides
    private final SimpleEnergyStorage ENERGY_STORAGE = new SimpleEnergyStorage(CAPACITY.get(), maxReceive) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            ModMessages.sendToClients(new EnergySyncS2CPacket(this.energy, getBlockPos()));
        }
    };
    private final LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);

    public TurtleChargingStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.TURTLE_CHARGING_STATION.get(), pPos, pBlockState);
    }

    // Energy stuff
    private int extractCount = 6; // Track if a turtle was charged this tick
    private int textureTimer = 10;

    @Override
    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        if (this.getBlockState().getValue(TurtleChargingStationBlock.ENABLED)) {
            // Check every direction for turtles
            for (Direction direction : Direction.values()) {
                BlockEntity be = level.getBlockEntity(this.worldPosition.relative(direction));
                // If no block entity is found return
                if (be == null) {
                    this.extractCount--;
                    continue;
                }
                // Check if block entity is a turtle
                if (be.getBlockState().getBlock() == ModRegistry.Blocks.TURTLE_NORMAL.get() ||
                        be.getBlockState().getBlock() == ModRegistry.Blocks.TURTLE_ADVANCED.get()) {

                    // If enough energy and no redstone signal
                    if (this.ENERGY_STORAGE.getEnergyStored() >= CONVERSION_RATE.get()) {
                        TurtleBlockEntity turtle = (TurtleBlockEntity) be;
                        if (turtle.getAccess().getFuelLevel() == turtle.getAccess().getFuelLimit()) {
                            this.extractCount--;
                        } else {
                            level.setBlock(pos, state.setValue(TurtleChargingStationBlock.CHARGING, true), 2);
                            this.refuelTurtle(turtle);
                            this.extractCount++;
                            // Sync with client for gui
                            ModMessages.sendToClients(new TurtleFuelSyncS2CPacket(turtle.getAccess().getFuelLevel(), turtle.getBlockPos()));
                        }
                    } else {
                        this.extractCount--;
                    }

                } else {
                    this.extractCount--;
                }
                // End of direction for-loop
            }

            if (this.extractCount <= 0) {
                if (--this.textureTimer <= 0) {
                    level.setBlock(pos, state.setValue(TurtleChargingStationBlock.CHARGING, false), 2);
                    this.textureTimer = 0;
                }
            } else {
                this.textureTimer = 10;
            }
            this.extractCount = 6;
        } else {
            level.setBlock(pos, state.setValue(TurtleChargingStationBlock.CHARGING, false), 2);
        }
    }

    private void refuelTurtle(TurtleBlockEntity turtle) {
       turtle.getAccess().addFuel(1);
       this.ENERGY_STORAGE.extractEnergy(CONVERSION_RATE.get(), false);
    }

    public EnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void setClientEnergy(int energy) {
        this.ENERGY_STORAGE.setEnergy(energy);
    }

    // Used for debug purposes
//    private static void debugRecharge(Level level, BlockPos pos, BlockState state, TurtleChargingStationBlockEntity chargingStation) {
//        BlockEntity blockEntity = level.getBlockEntity(pos.above());
//        if (blockEntity instanceof BeaconBlockEntity) {
//            chargingStation.setClientEnergy(Math.min(chargingStation.ENERGY_STORAGE.getMaxEnergyStored(),
//                    chargingStation.ENERGY_STORAGE.getEnergyStored() + 1200));
//        }
//    }

    // Gui
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new TurtleChargingStationMenu(id, inventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.turtlechargingstation.turtle_charging_station");
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
        ENERGY_STORAGE.setEnergy(nbt.getInt("turtle_charger.energy"));
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt("turtle_charger.energy", ENERGY_STORAGE.getEnergyStored());
        super.saveAdditional(nbt);
    }

}
