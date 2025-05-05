package com.mikitellurium.turtlechargingstation.util;

import com.mikitellurium.turtlechargingstation.common.block.CopperCableBlock;
import com.mikitellurium.turtlechargingstation.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.EnumMap;
import java.util.Map;

public class CableHelper {
    private static final double MIN = 5.0D;
    private static final double MAX = 11.0D;
    private static final VoxelShape CABLE_CORE_SHAPE = Block.createCuboidShape(5.0D, 5.0D, 5.0D, 11.0D, 11.0D, 11.0D);
    private static final Map<Direction, VoxelShape> CABLE_ARM_SHAPE = Util.make(new EnumMap<>(Direction.class), (map) -> {
        map.put(Direction.DOWN, Block.createCuboidShape(MIN, 0, MIN, MAX, MIN, MAX));
        map.put(Direction.UP, Block.createCuboidShape(MIN, MAX, MIN, MAX, 16.0D, MAX));
        map.put(Direction.NORTH, Block.createCuboidShape(MIN, MIN, 0, MAX, MAX, MIN));
        map.put(Direction.SOUTH, Block.createCuboidShape(MIN, MIN, MAX, MAX, MAX,16));
        map.put(Direction.WEST, Block.createCuboidShape(0, MIN, MIN, MIN, MAX, MAX));
        map.put(Direction.EAST, Block.createCuboidShape(MAX, MIN, MIN, 16, MAX, MAX));
    });
    private static final VoxelShape[] CABLE_SHAPES = new VoxelShape[64];

    private static int getShapeIndex(BlockState state) {
        int connections = 0;
        for (Direction dir : Direction.values()) {
            if (state.get(CopperCableBlock.CONNECTIONS.get(dir))) {
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
                shape = VoxelShapes.union(shape, CABLE_ARM_SHAPE.get(dir));
            }
        }
        return CABLE_SHAPES[index] = shape;
    }

    public static VoxelShape getCableShape(BlockState state) {
        return state.isOf(ModBlocks.COPPER_CABLE) ? getShape(getShapeIndex(state)) : VoxelShapes.empty();
    }
}
