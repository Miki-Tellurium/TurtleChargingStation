package com.mikitellurium.turtlechargingstation.client.gui;

import com.mikitellurium.telluriumforge.gui.menu.QuickMoveScreenHandler;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlechargingstation.registry.ModScreenHandlers;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;

public class TurtleChargingStationScreenHandler extends QuickMoveScreenHandler {

    private final TurtleChargingStationBlockEntity blockEntity;
    private final int invYOffset = 31;

    public TurtleChargingStationScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
    }

    public TurtleChargingStationScreenHandler(int id, PlayerInventory inventory, BlockEntity entity) {
        super(ModScreenHandlers.TURTLE_CHARGING_STATION, id, 1);
        this.blockEntity = (TurtleChargingStationBlockEntity) entity;
        this.addPlayerInventory(inventory);
        this.addPlayerHotbar(inventory);
        this.addSlot(new Slot(blockEntity.getInventory(), 0, 8, 87));
    }

    @Override
    public boolean isItemValid(int i, ItemStack itemStack) {
        return blockEntity.getInventory().isValid(i, itemStack);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.blockEntity.getInventory().canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory inventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(inventory, l + i * 9 + 9, l * 18 + 24, 84 + i * 18 + invYOffset));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory inventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, i * 18 + 24, 142 + invYOffset));
        }
    }

    public TurtleChargingStationBlockEntity getBlockEntity() {
        return this.blockEntity;
    }
}
