package com.mikitellurium.turtlechargingstation.common.integration.computercraft;

import com.mikitellurium.turtlechargingstation.common.block.TurtleChargingStationBlock;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationTileEntity;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class TurtleChargingStationPeripheral implements IPeripheral {
    private final TurtleChargingStationTileEntity tile;

    public TurtleChargingStationPeripheral(TurtleChargingStationTileEntity tile) {
        this.tile = tile;
    }

    @Nonnull
    @Override
    public String getType() {
        return "turtle_charging_station";
    }

    @Nonnull
    @Override
    public String[] getMethodNames() {
        return new String[]{"isEnabled", "isCharging", "getSide", "getSides", "getEnergy", "getEnergyCapacity", "getItemDetail"};
    }

    @Nullable
    @Override
    public Object[] callMethod(@Nonnull IComputerAccess iComputerAccess, @Nonnull ILuaContext iLuaContext, int method, @Nonnull Object[] arguments) throws LuaException, InterruptedException {
        TurtleChargingStationTileEntity.CCAccess access = tile.getAccess();
        switch (method) {
            case 0:
                return isEnabled(access);
            case 1:
                return isCharging(access);
            case 2:
                EnumFacing side = EnumFacing.byName(arguments[0].toString());
                if (side == null) {
                    throw new LuaException("Invalid side: " + arguments[0]);
                }
                return getSide(access, side);
            case 3:
                return getSides(access);
            case 4:
                return ret(tile.getEnergy());
            case 5:
                return ret(tile.getMaxEnergy());
            case 6:
                return getItemDetail(tile.getItemHandler().getStackInSlot(0));
            default:
                return null;
        }
    }

    public Object[] isEnabled(TurtleChargingStationTileEntity.CCAccess access) {
        return ret(access.getBlockState().getValue(TurtleChargingStationBlock.ENABLED));
    }

    public Object[] isCharging(TurtleChargingStationTileEntity.CCAccess access) {
        IBlockState blockState = access.getBlockState();
        return ret(blockState.getValue(TurtleChargingStationBlock.ENABLED) && blockState.getValue(TurtleChargingStationBlock.CHARGING));
    }

    public Object[] getSide(TurtleChargingStationTileEntity.CCAccess access, EnumFacing side) {
        Optional<Object> optional = this.getSideData(access.getWorld(), access.getPos(), side);
        return optional.map(this::ret).orElse(null);
    }

    public Object[] getSides(TurtleChargingStationTileEntity.CCAccess access) {
        Map<String, Object> results = new HashMap<>();
        for (EnumFacing direction : EnumFacing.values()) {
            Optional<Object> optional = this.getSideData(access.getWorld(), access.getPos(), direction);
            optional.ifPresent((obj) -> results.put(direction.getName(), obj));
        }
        return !results.isEmpty() ? ret(results) : null;
    }

    private Optional<Object> getSideData(World world, BlockPos pos, EnumFacing side) {
        TileEntity tile1 = world.getTileEntity(pos.offset(side));
        if (tile1 instanceof TileTurtle) {
            TileTurtle turtle = (TileTurtle) tile1;
            String label = turtle.hasCustomName() ? turtle.getLabel() : String.valueOf(turtle.getComputerID());
            Map<String, Object> sides = new HashMap<>();
            sides.put("name", label);
            sides.put("fuelLevel", turtle.getAccess().getFuelLevel());
            return Optional.of(sides);
        }
        return Optional.empty();
    }

    private Object[] getItemDetail(ItemStack stack) {
        if (stack.isEmpty()) {
            return ret((Object) null);
        } else {
            Item item = stack.getItem();
            String name = Item.REGISTRY.getNameForObject(item).toString();
            Map<String, Object> table = new HashMap<>();
            table.put("name", name);
            table.put("damage", stack.getItemDamage());
            table.put("count", stack.getCount());
            return ret(table);
        }
    }

    private Object[] ret(Object... objects) {
        return objects;
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof TurtleChargingStationPeripheral && this.tile == ((TurtleChargingStationPeripheral)other).tile;
    }
}
