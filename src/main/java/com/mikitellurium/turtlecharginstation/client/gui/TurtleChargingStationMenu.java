package com.mikitellurium.turtlecharginstation.client.gui;

import com.mikitellurium.telluriumforge.gui.menu.QuickMoveContainerMenu;
import com.mikitellurium.turtlecharginstation.common.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlecharginstation.registry.ModBlocks;
import com.mikitellurium.turtlecharginstation.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class TurtleChargingStationMenu extends QuickMoveContainerMenu {

    private final TurtleChargingStationBlockEntity blockEntity;
    private final Level level;
    private final int invYOffset = 31;

    public TurtleChargingStationMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        this(id, inventory, inventory.player.level().getBlockEntity(data.readBlockPos()));
    }

    public TurtleChargingStationMenu(int id, Inventory inventory, BlockEntity entity) {
        super(ModMenuTypes.TURTLE_CHARGING_STATION.get(), id, 1);
        blockEntity = (TurtleChargingStationBlockEntity) entity;
        this.level = inventory.player.level();
        this.addPlayerInventory(inventory);
        this.addPlayerHotbar(inventory);

        this.getBlockEntity().getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent((itemHandler) -> {
            this.addSlot(new SlotItemHandler(itemHandler, 0, 8, 87));
        });
    }

    @Override
    public boolean isItemValid(int i, ItemStack itemStack) {
        return blockEntity.getItemHandler().isItemValid(i, itemStack);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.TURTLE_CHARGING_STATION.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, l * 18 + 24, 84 + i * 18 + invYOffset));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, i * 18 + 24, 142 + invYOffset));
        }
    }

    public TurtleChargingStationBlockEntity getBlockEntity() {
        return blockEntity;
    }

}
