package com.mikitellurium.turtlechargingstation.common.blockentity;

import com.mikitellurium.telluriumforge.tileentity.ISideTickingTile;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TurtleChargingStationTileEntity extends TileEntity implements ISideTickingTile, IInteractionObject {
    private final ItemStackHandler itemHandler = new ItemStackHandler() {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };

    @Override
    public void update() {
        this.tickTile(this);
    }

    @Override
    public void tickServer(WorldServer world, IBlockState state, BlockPos pos) {

    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return newState.getBlock() != oldState.getBlock();
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new TurtleChargingStationContainer(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return this.getBlockType().getRegistryName().toString();
    }

    @Override
    public String getName() {
        return "Turtle Charging Station <-- add custom name";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(this.getName());
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }
}
