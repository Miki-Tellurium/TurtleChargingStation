package com.mikitellurium.turtlechargingstation.common.integration.computercraft;

import com.mikitellurium.turtlechargingstation.common.block.TurtleChargingStationBlock;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlechargingstation.registry.ModBlocks;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import dan200.computercraft.api.peripheral.PeripheralType;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("ConstantConditions")
public class TurtleChargingStationPeripheral implements GenericPeripheral {
    @Override
    public String id() {
        return ModBlocks.TURTLE_CHARGING_STATION.getId().toString();
    }

    @Override
    public PeripheralType getType() {
        return PeripheralType.ofAdditional("turtle_charging_station");
    }

    @LuaFunction(mainThread = true)
    public final boolean isEnabled(TurtleChargingStationBlockEntity.CCAccess access) {
        return access.getBlockState().getValue(TurtleChargingStationBlock.ENABLED);
    }

    @LuaFunction(mainThread = true)
    public final boolean isCharging(TurtleChargingStationBlockEntity.CCAccess access) {
        BlockState blockState = access.getBlockState();
        return blockState.getValue(TurtleChargingStationBlock.ENABLED) && blockState.getValue(TurtleChargingStationBlock.CHARGING);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getSide(TurtleChargingStationBlockEntity.CCAccess access, Direction side) {
        Optional<Object> optional = this.getSideData(access.getLevel(), access.getPos(), side);
        return MethodResult.of(optional.orElse(null));
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getSides(TurtleChargingStationBlockEntity.CCAccess access) {
        Map<String, Object> results = new HashMap<>();
        for (Direction direction : Direction.values()) {
            Optional<Object> optional = this.getSideData(access.getLevel(), access.getPos(), direction);
            optional.ifPresent((obj) -> results.put(direction.getName(), obj));
        }
        return MethodResult.of(!results.isEmpty() ? results : null);
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
