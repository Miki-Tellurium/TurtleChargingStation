package com.mikitellurium.turtlecharginstation.block;

import com.mikitellurium.telluriumforge.block.WaterloggedHelper;
import com.mikitellurium.telluriumforge.util.LogUtils;
import com.mikitellurium.turtlecharginstation.blockentity.CopperCableBlockEntity;
import com.mikitellurium.turtlecharginstation.registry.ModBlockEntities;
import com.mikitellurium.turtlecharginstation.registry.ModBlocks;
import com.mikitellurium.turtlecharginstation.util.CableHelper;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class CopperCableBlock extends BaseEntityBlock implements WaterloggedHelper {

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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CopperCableBlockEntity(pos, state);
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide && pHand == InteractionHand.MAIN_HAND) {
            pLevel.getBlockEntity(pPos, ModBlockEntities.COPPER_CABLE.get()).ifPresent((cable) -> {
                if (cable.hasNetwork()) {
                    cable.getNetwork().update(cable);
                    LogUtils.consoleLog(cable.getNetwork());
                }
            });
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
        return CableHelper.getCableShape(blockState);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        this.updateFluid(blockState, level, pos);
        BlockState newState = this.updateBlockState(level, blockState, pos);
        if (!level.isClientSide() && newState != blockState) { // Only update network if shape changed
            level.getBlockEntity(pos, ModBlockEntities.COPPER_CABLE.get()).ifPresent(CopperCableBlockEntity::updateConnections);
        }
        return newState;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        state.updateNeighbourShapes(level, pos, 3);
        if (!level.isClientSide) {
            level.getBlockEntity(pos, ModBlockEntities.COPPER_CABLE.get()).ifPresent(CopperCableBlockEntity::remove);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    private BlockState updateBlockState(LevelAccessor level, BlockState blockState, BlockPos pos) {
        BlockState newState = blockState;
        for (Direction direction : Direction.values()) {
            boolean shouldConnect;
            BlockPos relativePos = pos.relative(direction);
            BlockState neighbourState = level.getBlockState(relativePos);
            if (neighbourState.is(ModBlocks.COPPER_CABLE.get())) {
                shouldConnect = true;
            } else {
                BlockEntity blockEntity = level.getBlockEntity(relativePos);
                shouldConnect = blockEntity != null && blockEntity.getCapability(ForgeCapabilities.ENERGY).isPresent();
            }
            newState = newState.setValue(CONNECTIONS.get(direction), shouldConnect);
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
