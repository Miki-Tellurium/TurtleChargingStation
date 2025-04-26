package com.mikitellurium.turtlechargingstation.util;

import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ConductiveBlockContext {

    private static final Map<Property<?>, DirectionFunction> PROPERTY_FUNCTIONS = Util.make(new HashMap<>(), (map) -> {
        map.put(BlockStateProperties.FACING, new DirectionFunction(
                (state) -> new Direction[]{state.getValue(BlockStateProperties.FACING)},
                (state) -> state.getValue(BlockStateProperties.FACING).getAxis()
        ));
        map.put(BlockStateProperties.HORIZONTAL_FACING, new DirectionFunction(
                (state) -> new Direction[]{state.getValue(BlockStateProperties.HORIZONTAL_FACING)},
                (state) -> state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis()
        ));
        map.put(BlockStateProperties.VERTICAL_DIRECTION, new DirectionFunction(
                (state) -> new Direction[]{state.getValue(BlockStateProperties.VERTICAL_DIRECTION)},
                (state) -> state.getValue(BlockStateProperties.VERTICAL_DIRECTION).getAxis()
        ));
        map.put(BlockStateProperties.AXIS, new DirectionFunction(
                (state) -> {
                    Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                    return new Direction[]{
                            Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE),
                            Direction.fromAxisAndDirection(axis, Direction.AxisDirection.NEGATIVE)};
                },
                (state) -> state.getValue(BlockStateProperties.AXIS)
        ));
        map.put(BlockStateProperties.HORIZONTAL_AXIS, new DirectionFunction(
                (state) -> {
                    Direction.Axis axis = state.getValue(BlockStateProperties.HORIZONTAL_AXIS);
                    return new Direction[]{
                            Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE),
                            Direction.fromAxisAndDirection(axis, Direction.AxisDirection.NEGATIVE)};
                },
                (state) -> state.getValue(BlockStateProperties.HORIZONTAL_AXIS)));
    });

    private final BlockState blockState;
    private final Direction[] directions;
    private final Direction.Axis axis;

    public ConductiveBlockContext(BlockState blockState) {
        this.blockState = blockState;
        DirectionFunction function = DirectionFunction.getFunction(blockState);
        this.directions = function.getDirections(blockState);
        this.axis = function.getAxis(blockState);
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public Direction[] getDirections() {
        return directions;
    }

    public Direction.Axis getAxis() {
        return axis;
    }

    // If axis is null the block can conduct to any directions
    public boolean canConductTo(BlockState blockState, Direction direction) {
        ConductiveBlockContext context = new ConductiveBlockContext(blockState);
        return (this.axis == null && context.axis != null) ? context.axis.test(direction) :
                (this.axis != null && context.axis == null) ? this.axis.test(direction) :
                        this.axis == context.axis;
    }

    private record DirectionFunction(Function<BlockState, Direction[]> directionFunction,
                                     Function<BlockState, Direction.Axis> axisFunction) {
        private static final DirectionFunction DEFAULT = new DirectionFunction(
                (state) -> Direction.values(),
                (state) -> null);

        Direction[] getDirections(BlockState blockState) {
            return this.directionFunction.apply(blockState);
        }

        Direction.Axis getAxis(BlockState blockState) {
            return this.axisFunction.apply(blockState);
        }

        static DirectionFunction getFunction(BlockState blockState) {
            for (Map.Entry<Property<?>, DirectionFunction> entry : PROPERTY_FUNCTIONS.entrySet()) {
                Property<?> property = entry.getKey();
                if (blockState.hasProperty(property)) {
                    return entry.getValue();
                }
            }
            return DirectionFunction.DEFAULT;
        }
    }

}
