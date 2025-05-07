package com.mikitellurium.turtlechargingstation.common.blockentity;

import com.mikitellurium.telluriumforge.blockentity.NameableBlockEntity;
import com.mikitellurium.telluriumforge.blockentity.TickingBlockEntity;
import com.mikitellurium.telluriumforge.config.RangedConfigEntry;
import com.mikitellurium.telluriumforge.networking.NetworkingHelper;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationScreenHandler;
import com.mikitellurium.turtlechargingstation.common.block.TurtleChargingStationBlock;
import com.mikitellurium.turtlechargingstation.common.energy.ModEnergyStorage;
import com.mikitellurium.turtlechargingstation.networking.packets.EnergySyncPacket;
import com.mikitellurium.turtlechargingstation.networking.packets.TurtleFuelSyncPacket;
import com.mikitellurium.turtlechargingstation.registry.ModBlockEntities;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class TurtleChargingStationBlockEntity extends NameableBlockEntity implements ExtendedScreenHandlerFactory, TickingBlockEntity {

    public static RangedConfigEntry<Long> CAPACITY;
    public static RangedConfigEntry<Long> CONVERSION_RATE; // Based on Thermal Expansion stirling dynamo production rate using coal
    private final long maxReceive = CONVERSION_RATE.get() * 6; // 6 sides
    private final ModEnergyStorage energyStorage = new ModEnergyStorage(CAPACITY.get(), maxReceive) {
        @SuppressWarnings("DataFlowIssue")
        @Override
        protected void onFinalCommit() {
            markDirty();
            if (!world.isClient) {
                NetworkingHelper.sendToTrackingClients((ServerWorld) world, pos, new EnergySyncPacket(pos, this.getAmount()));
            }
        }
    };
    private final SimpleInventory inventory = new SimpleInventory(1) {
        @Override
        public void markDirty() {
            super.markDirty();
            TurtleChargingStationBlockEntity.this.markDirty();
        }
    };
    private final int textureChangeDelay = (int) Math.ceil((double) CONVERSION_RATE.get() / ThunderchargeDynamoBlockEntity.TRANSFER_RATE.get()) + 1;
    private int textureTimer = 0;

    public TurtleChargingStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.TURTLE_CHARGING_STATION, pPos, pBlockState);
    }

    @Override
    public void serverTick(ServerWorld world, BlockPos pos, BlockState state) {
        ItemStack itemStack = this.inventory.getStack(0);
        EnergyStorage energyStorage1 = EnergyStorage.ITEM.find(itemStack, ContainerItemContext.withConstant(itemStack));
        if (energyStorage1 != null) {
            long extracted;
            try (Transaction transaction = Transaction.openOuter()) {
                extracted = energyStorage1.extract(this.maxReceive, transaction);
                transaction.abort();
            }
            try (Transaction transaction = Transaction.openOuter()) {
                if (extracted > 0 && this.getEnergy() < this.getEnergyCapacity()) {
                    long energy = this.energyStorage.insert(extracted, transaction);
                    energyStorage1.extract(energy, transaction);
                    transaction.commit();
                }
            }
        }
        List<TurtleBlockEntity> turtles = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            BlockEntity be = world.getBlockEntity(this.pos.offset(direction));
            if (be == null) continue;
            if (be instanceof TurtleBlockEntity) {
                turtles.add((TurtleBlockEntity) be);
            }
        }

        // State stays charging even if disabled
        boolean shouldCharge = !turtles.isEmpty() && this.hasChargeableTurtle(turtles) && this.energyStorage.getAmount() >= CONVERSION_RATE.get();
        world.setBlockState(pos, state.with(TurtleChargingStationBlock.CHARGING, shouldCharge || textureTimer > 0), 2);
        if (shouldCharge && this.getCachedState().get(TurtleChargingStationBlock.ENABLED)) {
            for (TurtleBlockEntity turtle : turtles) {
                if (this.isChargeable(turtle) && this.energyStorage.getAmount() >= CONVERSION_RATE.get()) {
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
        try (Transaction transaction = Transaction.openOuter()) {
            if (this.energyStorage.extract(CONVERSION_RATE.get(), transaction) == CONVERSION_RATE.get()) {
                turtle.getAccess().addFuel(1);
                transaction.commit();
                NetworkingHelper.sendToTrackingClients((ServerWorld) this.world, this.pos, new TurtleFuelSyncPacket(turtle.getPos(), turtle.getAccess().getFuelLevel()));
            }
        }
    }

    public EnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    public long getEnergy() {
        return this.energyStorage.getAmount();
    }

    public long getEnergyCapacity() {
        return this.energyStorage.getCapacity();
    }

    public void setAmount(long amount) {
        this.energyStorage.setAmount(amount);
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public EnergyStorage energyLookup(Direction side) {
        return this.energyStorage;
    }

    public InventoryStorage inventoryLookup(Direction side) {
        return InventoryStorage.of(this.inventory, side);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new TurtleChargingStationScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeLong(energyStorage.getAmount());
    }

    @Override
    protected Text getDefaultName() {
        return Text.translatable("block.turtlechargingstation.turtle_charging_station");
    }

    @Override
    public Text getDisplayName() {
        return this.getName();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        energyStorage.setAmount(nbt.getLong("energy"));
        inventory.readNbtList(nbt.getList("inventory", NbtElement.COMPOUND_TYPE));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putLong("energy", energyStorage.getAmount());
        nbt.put("inventory", inventory.toNbtList());
        super.readNbt(nbt);
    }

}
