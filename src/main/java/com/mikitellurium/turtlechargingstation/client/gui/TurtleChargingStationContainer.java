package com.mikitellurium.turtlechargingstation.client.gui;


import com.mikitellurium.telluriumforge.gui.QuickMoveContainer;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.SlotItemHandler;

public class TurtleChargingStationContainer extends QuickMoveContainer {
    private final TurtleChargingStationTileEntity tile;

    public TurtleChargingStationContainer(IInventory playerInv, TurtleChargingStationTileEntity tile) {
        super(playerInv, 1, 31);
        this.tile = tile;
        this.addSlotToContainer(new SlotItemHandler(tile.getItemHandler(), 0, 8, 87));
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return player.getDistanceSqToCenter(tile.getPos()) <= 64.0D;
    }

    public TurtleChargingStationTileEntity getTile() {
        return tile;
    }
}
