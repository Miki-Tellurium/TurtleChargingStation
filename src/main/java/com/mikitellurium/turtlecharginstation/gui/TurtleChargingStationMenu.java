package com.mikitellurium.turtlecharginstation.gui;

import com.mikitellurium.turtlecharginstation.registry.ModBlocks;
import com.mikitellurium.turtlecharginstation.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlecharginstation.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

public class TurtleChargingStationMenu extends AbstractContainerMenu {

    private final TurtleChargingStationBlockEntity blockEntity;
    private final Level level;

    public TurtleChargingStationMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        this(id, inventory, inventory.player.level().getBlockEntity(data.readBlockPos()));
    }

    public TurtleChargingStationMenu(int id, Inventory inventory, BlockEntity entity) {
        super(ModMenuTypes.TURTLE_CHARGING_STATION_GUI.get(), id);
        blockEntity = (TurtleChargingStationBlockEntity) entity;
        this.level = inventory.player.level();
    }

    public int getEnergy() {
        return blockEntity.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer,
                ModBlocks.TURTLE_CHARGING_STATION_BLOCK.get());
    }

    public TurtleChargingStationBlockEntity getBlockEntity() {
        return blockEntity;
    }

}
