package com.mikitellurium.turtlechargingstation.common.blockentity;

import com.mikitellurium.telluriumforge.energy.SimpleEnergyStorage;
import com.mikitellurium.telluriumforge.tileentity.ISideTickingTile;
import com.mikitellurium.turtlechargingstation.TurtleChargingStationMod;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationContainer;
import com.mikitellurium.turtlechargingstation.common.block.TurtleChargingStationBlock;
import com.mikitellurium.turtlechargingstation.networking.ModNetworking;
import com.mikitellurium.turtlechargingstation.networking.packet.EnergySyncS2CPacket;
import com.mikitellurium.turtlechargingstation.networking.packet.TurtleFuelSyncS2CPacket;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.common.config.Config;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Config(modid = TurtleChargingStationMod.MOD_ID)
public class TurtleChargingStationTileEntity extends TileEntity implements ISideTickingTile, IInteractionObject {
    @Config.Name("chargingStationMaxCapacity")
    @Config.Comment("The maximum amount of FE the charging station can hold.")
    @Config.RangeInt(min = 0)
    public static int CAPACITY = 64000;
    @Config.Name("chargingStationConversionRate")
    @Config.Comment("The amount of FE required to increase the turtle fuel level by 1.")
    @Config.RangeInt(min = 0)
    public static int CONVERSION_RATE = 256; // Based on Thermal Expansion stirling dynamo production rate using coal

    private String customName;
    private final int maxReceive = CONVERSION_RATE * 6; // 6 sides
    private final ItemStackHandler itemHandler = new ItemStackHandler() {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    private final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(CAPACITY, maxReceive) {
        @Override
        public void onEnergyChanged() {
            markDirty();
            ModNetworking.INSTANCE.sendToAll(new EnergySyncS2CPacket(pos, this.energy));
        }
    };
    private final int textureChangeDelay = (int) Math.ceil((double) CONVERSION_RATE / 1024) + 1;
    private int textureTimer = 0;

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return newState.getBlock() != oldState.getBlock();
    }

    @Override
    public void update() {
        this.tickTile(this);
    }

    @Override
    public void tickServer(WorldServer world, IBlockState state, BlockPos pos) {
        ItemStack itemStack = this.itemHandler.getStackInSlot(0);
        if (itemStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
            IEnergyStorage stackStorage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
            int extracted = stackStorage.extractEnergy(this.maxReceive, true);
            if (extracted > 0 && this.getEnergy() < this.getMaxEnergy()) {
                int energy = this.energyStorage.receiveEnergy(extracted, false);
                stackStorage.extractEnergy(energy, false);
            }
        }

        List<TileTurtle> turtles = new ArrayList<>();
        for (EnumFacing direction : EnumFacing.values()) {
            TileEntity tile = world.getTileEntity(this.pos.offset(direction));
            if (tile == null) continue;
            if (tile instanceof TileTurtle) {
                turtles.add((TileTurtle) tile);
            }
        }
        // State stays charging even if disabled
        boolean shouldCharge = !turtles.isEmpty() && this.hasChargeableTurtle(turtles) && this.energyStorage.getEnergyStored() >= CONVERSION_RATE;
        world.setBlockState(pos, state.withProperty(TurtleChargingStationBlock.CHARGING, shouldCharge || textureTimer > 0), 2);
        if (shouldCharge && world.getBlockState(pos).getValue(TurtleChargingStationBlock.ENABLED)) {
            for (TileTurtle turtle: turtles) {
                if (this.isChargeable(turtle) && this.energyStorage.getEnergyStored() >= CONVERSION_RATE) {
                    this.refuelTurtle(turtle);
                    textureTimer = textureChangeDelay;
                }
            }
        }
        if (textureTimer > 0) {
            textureTimer--;
        }
    }

    private boolean hasChargeableTurtle(List<TileTurtle> turtles) {
        for (TileTurtle turtle: turtles) {
            if (this.isChargeable(turtle)) {
                return true;
            }
        }
        return false;
    }

    private boolean isChargeable(TileTurtle turtle) {
        return turtle.getAccess().getFuelLevel() < turtle.getAccess().getFuelLimit();
    }

    private void refuelTurtle(TileTurtle turtle) {
        if (this.energyStorage.extractEnergy(CONVERSION_RATE, false) == CONVERSION_RATE) {
            turtle.getAccess().addFuel(1);
            ModNetworking.INSTANCE.sendToAll(new TurtleFuelSyncS2CPacket(turtle.getPos(), turtle.getAccess().getFuelLevel()));
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
        return this.hasCustomName() ? this.customName : this.getBlockType().getLocalizedName();
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
        } else if (capability == CapabilityEnergy.ENERGY) {
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
            return CapabilityEnergy.ENERGY.cast(energyStorage);
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
