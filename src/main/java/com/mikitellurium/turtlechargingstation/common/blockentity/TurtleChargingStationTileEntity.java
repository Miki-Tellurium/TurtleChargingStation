package com.mikitellurium.turtlechargingstation.common.blockentity;

import com.mikitellurium.telluriumforge.energy.SimpleEnergyStorage;
import com.mikitellurium.telluriumforge.tileentity.ISideTickingTile;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationContainer;
import com.mikitellurium.turtlechargingstation.networking.ModNetworking;
import com.mikitellurium.turtlechargingstation.networking.packet.EnergySyncS2CPacket;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TurtleChargingStationTileEntity extends TileEntity implements ISideTickingTile, IInteractionObject {
    private String customName;
    private final ItemStackHandler itemHandler = new ItemStackHandler() {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    private final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(256000, 1024) {
        @Override
        public void onEnergyChanged() {
            markDirty();
            ModNetworking.INSTANCE.sendToAll(new EnergySyncS2CPacket(pos, this.energy));
        }
    };

    @Override
    public void update() {
        this.tickTile(this);
    }

    @Override
    public void tickServer(WorldServer world, IBlockState state, BlockPos pos) {
        if (world.getTileEntity(pos.up()) instanceof TileEntityBeacon) {
            energyStorage.receiveEnergy(10000, false);
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return newState.getBlock() != oldState.getBlock();
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
        return this.hasCustomName() ? this.customName : this.getBlockType().getTranslationKey();
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
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
        } else if (capability == CapabilityEnergy.ENERGY) {
            CapabilityEnergy.ENERGY.cast(energyStorage);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        energyStorage.setEnergy(nbt.getInteger("turtle_charging_station.energy"));
        itemHandler.deserializeNBT(nbt.getCompoundTag("turtle_charging_station.inventory"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("turtle_charging_station.energy", energyStorage.getEnergyStored());
        nbt.setTag("turtle_charging_station.inventory", itemHandler.serializeNBT());
        return super.writeToNBT(nbt);
    }
}
