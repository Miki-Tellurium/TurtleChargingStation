package com.mikitellurium.turtlechargingstation.common.blockentity;

import com.mikitellurium.telluriumforge.blockentity.NameableBlockEntity;
import com.mikitellurium.telluriumforge.blockentity.TickingBlockEntity;
import com.mikitellurium.telluriumforge.energy.SimpleEnergyStorage;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationMenu;
import com.mikitellurium.turtlechargingstation.common.block.TurtleChargingStationBlock;
import com.mikitellurium.turtlechargingstation.networking.Networking;
import com.mikitellurium.turtlechargingstation.networking.packets.EnergySyncS2CPacket;
import com.mikitellurium.turtlechargingstation.networking.packets.TurtleFuelSyncS2CPacket;
import com.mikitellurium.turtlechargingstation.registry.ModBlockEntities;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TurtleChargingStationBlockEntity extends NameableBlockEntity implements TickingBlockEntity, MenuProvider {
    public static final Capability<CCAccess> ACCESS_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    public static ForgeConfigSpec.IntValue CAPACITY;
    public static ForgeConfigSpec.IntValue CONVERSION_RATE; // Based on Thermal Expansion stirling dynamo production rate using coal
    private final int maxReceive = CONVERSION_RATE.get() * 6; // 6 sides
    private final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(CAPACITY.get(), maxReceive) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            Networking.HELPER.sendToClients(new EnergySyncS2CPacket(this.energy, worldPosition));
        }
    };
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final CCAccess access = new CCAccess() {
        @Override
        public BlockPos getPos() {
            return worldPosition;
        }

        @Override
        public BlockState getBlockState() {
            return TurtleChargingStationBlockEntity.this.getBlockState();
        }

        @Override
        public Level getLevel() {
            return level;
        }
    };
    private final LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.of(() -> energyStorage);
    private final LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<CCAccess> lazyAccess = LazyOptional.of(() -> access);
    private final int textureChangeDelay = (int) Math.ceil((double) CONVERSION_RATE.get() / ThunderchargeDynamoBlockEntity.TRANSFER_RATE.get()) + 1;
    private int textureTimer = 0;

    public TurtleChargingStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.TURTLE_CHARGING_STATION.get(), pPos, pBlockState);
    }

    @Override
    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        ItemStack itemStack = this.itemHandler.getStackInSlot(0);
        itemStack.getCapability(ForgeCapabilities.ENERGY).ifPresent((energyStorage) -> {
            int extracted = energyStorage.extractEnergy(this.maxReceive, true);
            if (extracted > 0 && this.getEnergy() < this.getMaxEnergy()) {
                int energy = this.energyStorage.receiveEnergy(extracted, false);
                energyStorage.extractEnergy(energy, false);
            }
        });
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
        level.setBlock(pos, state.setValue(TurtleChargingStationBlock.CHARGING, shouldCharge || textureTimer > 0), 2);
        if (shouldCharge && this.getBlockState().getValue(TurtleChargingStationBlock.ENABLED)) {
            for (TurtleBlockEntity turtle: turtles) {
                if (this.isChargeable(turtle) && this.energyStorage.getEnergyStored() >= CONVERSION_RATE.get()) {
                    this.refuelTurtle(turtle);
                    textureTimer = textureChangeDelay;
                }
            }
        }
        if (textureTimer > 0) {
            textureTimer--;
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
        if (this.energyStorage.extractEnergy(CONVERSION_RATE.get(), false) == CONVERSION_RATE.get()) {
            turtle.getAccess().addFuel(1);
            Networking.HELPER.sendToClients(new TurtleFuelSyncS2CPacket(turtle.getAccess().getFuelLevel(), turtle.getBlockPos()));
        }
    }

    public EnergyStorage getEnergyStorage() {
        return this.energyStorage;
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

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
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

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        } else if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        } else if (cap == ACCESS_CAP) {
            return lazyAccess.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt("turtle_charger.energy", energyStorage.getEnergyStored());
        nbt.put("turtle_charger.inventory", itemHandler.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        energyStorage.setEnergy(nbt.getInt("turtle_charger.energy"));
        itemHandler.deserializeNBT(nbt.getCompound("turtle_charger.inventory"));
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyHandler.invalidate();
        lazyItemHandler.invalidate();
        lazyAccess.invalidate();
    }

    // Computercraft MethodSupplierImpl.getMethodsImpl() makes TickingBlockEntity.clientTick()
    // load ClientLevel class when running on dedicated server because it loads all method
    // from the block entity, including interfaces. Using this we expose charging station
    // data as a capability without loading methods from the block entity.
    public interface CCAccess {
        BlockPos getPos();
        BlockState getBlockState();
        Level getLevel();
    }
}
