package com.mikitellurium.turtlechargingstation.common.integration.computercraft;

import com.mikitellurium.turtlechargingstation.common.block.TurtleChargingStationBlock;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlechargingstation.registry.ModBlocks;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import dan200.computercraft.api.peripheral.PeripheralType;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("ConstantConditions")
public class TurtleChargingStationPeripheral implements GenericPeripheral {

    @Override
    public String id() {
        return Registries.BLOCK.getId(ModBlocks.TURTLE_CHARGING_STATION).toString();
    }

    @Override
    public PeripheralType getType() {
        return PeripheralType.ofAdditional("charging_station");
    }

    @LuaFunction(mainThread = true)
    public final long getEnergy(TurtleChargingStationBlockEntity chargingStation) {
        return chargingStation.getEnergy();
    }

    @LuaFunction(mainThread = true)
    public final long getEnergyCapacity(TurtleChargingStationBlockEntity chargingStation) {
        return chargingStation.getEnergyCapacity();
    }

    @LuaFunction(mainThread = true)
    public final boolean isEnabled(TurtleChargingStationBlockEntity chargingStation) {
        return chargingStation.getCachedState().get(TurtleChargingStationBlock.ENABLED);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getSide(TurtleChargingStationBlockEntity chargingStation, Direction side) {
        Optional<Object> optional = this.getSideData(chargingStation.getWorld(), chargingStation.getPos(), side);
        return MethodResult.of(optional.orElse(null));
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getSides(TurtleChargingStationBlockEntity chargingStation) {
        Map<String, Object> results = new HashMap<>();
        for (Direction direction : Direction.values()) {
            Optional<Object> optional = this.getSideData(chargingStation.getWorld(), chargingStation.getPos(), direction);
            optional.ifPresent((obj) -> results.put(direction.getName(), obj));
        }
        return MethodResult.of(!results.isEmpty() ? results : null);
    }

    private Optional<Object> getSideData(World world, BlockPos pos, Direction side) {
        BlockEntity blockEntity = world.getBlockEntity(pos.offset(side));
        if (blockEntity instanceof TurtleBlockEntity turtle) {
            String label = turtle.hasCustomName() ? turtle.getLabel() : String.valueOf(turtle.getComputerID());
            return Optional.of(Map.of("name", label, "fuelLevel", turtle.getAccess().getFuelLevel()));
        }
        return Optional.empty();
    }

}
