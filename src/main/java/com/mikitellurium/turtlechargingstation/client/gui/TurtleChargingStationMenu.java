package com.mikitellurium.turtlechargingstation.client.gui;

import com.mikitellurium.telluriumforge.capability.CapabilityHelper;
import com.mikitellurium.telluriumforge.gui.menu.QuickMoveContainerMenu;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlechargingstation.registry.ModBlocks;
import com.mikitellurium.turtlechargingstation.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.SlotItemHandler;

public class TurtleChargingStationMenu extends QuickMoveContainerMenu {

    private final TurtleChargingStationBlockEntity blockEntity;
    private final Level level;
    private final int invYOffset = 31;

    public TurtleChargingStationMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        this(id, inventory, inventory.player.level().getBlockEntity(data.readBlockPos()));
    }

    @SuppressWarnings("DataFlowIssue")
    public TurtleChargingStationMenu(int id, Inventory inventory, BlockEntity entity) {
        super(ModMenuTypes.TURTLE_CHARGING_STATION.get(), id, 1);
        blockEntity = (TurtleChargingStationBlockEntity) entity;
        this.level = inventory.player.level();
        this.addPlayerInventory(inventory);
        this.addPlayerHotbar(inventory);

        CapabilityHelper.getOptional(blockEntity.getLevel(), Capabilities.ItemHandler.BLOCK, blockEntity.getBlockPos(), null)
                .ifPresent((itemHandler) -> this.addSlot(new SlotItemHandler(itemHandler, 0, 8, 87)));
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
