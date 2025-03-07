package com.mikitellurium.turtlecharginstation.block;

import com.mikitellurium.telluriumforge.block.WaterloggedHelper;
import com.mikitellurium.turtlecharginstation.registry.ModBlocks;
import com.mikitellurium.turtlecharginstation.util.CableHelper;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

public class CopperCableBlock extends Block implements WaterloggedHelper {

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final Map<Direction, BooleanProperty> CONNECTIONS = Util.make(new EnumMap<>(Direction.class), (map) -> {
        map.put(Direction.DOWN, DOWN);
        map.put(Direction.UP, UP);
        map.put(Direction.EAST, EAST);
        map.put(Direction.WEST, WEST);
        map.put(Direction.NORTH, NORTH);
        map.put(Direction.SOUTH, SOUTH);
    });

    public CopperCableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(DOWN, false)
                .setValue(UP, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(WATERLOGGED, false));
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
        return CableHelper.getCableShape(blockState);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        this.updateFluid(blockState, level, pos);
        return this.updateConnections(level, blockState, pos);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        state.updateNeighbourShapes(level, pos, 3);
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    private BlockState updateConnections(LevelAccessor level, BlockState blockState, BlockPos pos) {
        BlockState newState = blockState;
        for (Direction direction : Direction.values()) {
            BlockState neighbourState = level.getBlockState(pos.relative(direction));
            newState = newState.setValue(CONNECTIONS.get(direction), neighbourState.is(ModBlocks.COPPER_CABLE.get()));
        }
        return newState;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(WATERLOGGED, this.getFluidStateForPlacement(context));
    }

    public FluidState getFluidState(BlockState blockState) {
        return this.getFluidForBlockState(blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DOWN, UP, EAST, WEST, NORTH, SOUTH, WATERLOGGED);
    }

}
