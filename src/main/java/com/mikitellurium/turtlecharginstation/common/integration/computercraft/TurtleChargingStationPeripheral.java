package com.mikitellurium.turtlecharginstation.common.integration.computercraft;

import com.mikitellurium.turtlecharginstation.common.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlecharginstation.registry.ModBlocks;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import dan200.computercraft.api.peripheral.PeripheralType;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;

@SuppressWarnings("ConstantConditions")
public class TurtleChargingStationPeripheral implements GenericPeripheral {

    @Override
    public String id() {
        return ModBlocks.TURTLE_CHARGING_STATION.getId().toString();
    }

    @Override
    public PeripheralType getType() {
        return PeripheralType.ofAdditional("charging_station");
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getSide(TurtleChargingStationBlockEntity chargingStation, Direction side) {
        Optional<Object> optional = this.getSideData(chargingStation.getLevel(), chargingStation.getBlockPos(), side);
        return MethodResult.of(optional.orElse(null));
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getSides(TurtleChargingStationBlockEntity chargingStation) {
        Map<String, Object> results = new HashMap<>();
        for (Direction direction : Direction.values()) {
            Optional<Object> optional = this.getSideData(chargingStation.getLevel(), chargingStation.getBlockPos(), direction);
            results.put(direction.getName(), optional.orElse(null));
        }
        return MethodResult.of(results);
    }

    private Optional<Object> getSideData(Level level, BlockPos pos, Direction side) {
        BlockEntity blockEntity = level.getExistingBlockEntity(pos.relative(side));
        if (blockEntity instanceof TurtleBlockEntity turtle) {
            String label = turtle.hasCustomName() ? turtle.getLabel() : String.valueOf(turtle.getComputerID());
            return Optional.of(Map.of("name", label, "fuelLevel", turtle.getAccess().getFuelLevel()));
        }
        return Optional.empty();
    }

}
