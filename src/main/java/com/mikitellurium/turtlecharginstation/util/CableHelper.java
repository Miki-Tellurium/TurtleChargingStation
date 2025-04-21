package com.mikitellurium.turtlecharginstation.util;

import com.mikitellurium.turtlecharginstation.common.block.CopperCableBlock;
import com.mikitellurium.turtlecharginstation.registry.ModBlocks;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

public class CableHelper {

    private static final double MIN = 5.0D;
    private static final double MAX = 11.0D;
    private static final VoxelShape CABLE_CORE_SHAPE = Block.box(5.0D, 5.0D, 5.0D, 11.0D, 11.0D, 11.0D);
    private static final Map<Direction, VoxelShape> CABLE_ARM_SHAPE = Util.make(new EnumMap<>(Direction.class), (map) -> {
        map.put(Direction.DOWN, Block.box(MIN, 0, MIN, MAX, MIN, MAX));
        map.put(Direction.UP, Block.box(MIN, MAX, MIN, MAX, 16.0D, MAX));
        map.put(Direction.NORTH, Block.box(MIN, MIN, 0, MAX, MAX, MIN));
        map.put(Direction.SOUTH, Block.box(MIN, MIN, MAX, MAX, MAX,16));
        map.put(Direction.WEST, Block.box(0, MIN, MIN, MIN, MAX, MAX));
        map.put(Direction.EAST, Block.box(MAX, MIN, MIN, 16, MAX, MAX));
    });
    private static final VoxelShape[] CABLE_SHAPES = new VoxelShape[64];

    private static int getShapeIndex(BlockState state) {
        int connections = 0;
        for (Direction dir : Direction.values()) {
            if (state.getValue(CopperCableBlock.CONNECTIONS.get(dir))) {
                connections |= 1 << dir.ordinal();
            }
        }
        return connections;
    }

    private static VoxelShape getShape(int index) {
        VoxelShape shape = CABLE_SHAPES[index];
        if (shape != null) {
            return shape;
        }
        shape = CABLE_CORE_SHAPE;
        for (Direction dir : Direction.values()) {
            if ((index & 1 << dir.ordinal()) != 0) {
                shape = Shapes.or(shape, CABLE_ARM_SHAPE.get(dir));
            }
        }
        return CABLE_SHAPES[index] = shape;
    }

    public static VoxelShape getCableShape(BlockState state) {
        return state.is(ModBlocks.COPPER_CABLE.get()) ? getShape(getShapeIndex(state)) : Shapes.empty();
    }

}
