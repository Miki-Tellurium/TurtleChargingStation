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
    public final boolean isEnabled(TurtleChargingStationBlockEntity.CCAccess access) {
        return access.getBlockState().get(TurtleChargingStationBlock.ENABLED);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getSide(TurtleChargingStationBlockEntity.CCAccess access, Direction side) {
        Optional<Object> optional = this.getSideData(access.getWorld(), access.getPos(), side);
        return MethodResult.of(optional.orElse(null));
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getSides(TurtleChargingStationBlockEntity.CCAccess access) {
        Map<String, Object> results = new HashMap<>();
        for (Direction direction : Direction.values()) {
            Optional<Object> optional = this.getSideData(access.getWorld(), access.getPos(), direction);
            optional.ifPresent((obj) -> results.put(direction.getName(), obj));
        }
        return MethodResult.of(!results.isEmpty() ? results : null);
    }

    @LuaFunction(mainThread = true)
    public final long getEnergy(TurtleChargingStationBlockEntity.CCAccess access) {
        return access.getEnergy();
    }

    @LuaFunction(mainThread = true)
    public final long getEnergyCapacity(TurtleChargingStationBlockEntity.CCAccess access) {
        return access.getEnergyCapacity();
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
