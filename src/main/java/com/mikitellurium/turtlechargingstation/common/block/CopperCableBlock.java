package com.mikitellurium.turtlechargingstation.common.block;

import com.mikitellurium.telluriumforge.block.WaterloggedHelper;
import com.mikitellurium.turtlechargingstation.common.blockentity.CopperCableBlockEntity;
import com.mikitellurium.turtlechargingstation.registry.ModBlockEntities;
import com.mikitellurium.turtlechargingstation.registry.ModBlocks;
import com.mikitellurium.turtlechargingstation.util.CableHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class CopperCableBlock extends BaseEntityBlock implements WaterloggedHelper {

    public static final BooleanProperty BURNING = BooleanProperty.create("burning");
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
                .setValue(BURNING, false)
                .setValue(DOWN, false)
                .setValue(UP, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CopperCableBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CopperCableBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.COPPER_CABLE.get(),
                (tickLevel, pos, state, blockEntity) -> blockEntity.tick(tickLevel, pos, state));
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos pos, RandomSource random) {
        if (blockState.getValue(BURNING) && random.nextFloat() < 0.7F) {
            Direction direction = Direction.getRandom(random);
            if (blockState.getValue(CONNECTIONS.get(direction))) {
                Vec3 vec3 = pos.getCenter();
                double d0 = direction.getStepX() == 0 ? getRandomOffset(random) : direction.getStepX() * (random.nextDouble() / 2);
                double d1 = direction.getStepY() == 0 ? getRandomOffset(random) : direction.getStepY() * (random.nextDouble() / 2);
                double d2 = direction.getStepZ() == 0 ? getRandomOffset(random) : direction.getStepZ() * (random.nextDouble() / 2);
                ParticleOptions particleType = blockState.getValue(WATERLOGGED) ? ParticleTypes.BUBBLE : ParticleTypes.SMOKE;
                double ySpeed = blockState.getValue(WATERLOGGED) ? 0.2D : -0.01D;
                level.addParticle(particleType, vec3.x + d0, vec3.y + d1, vec3.z + d2, 0, ySpeed, 0);
            }
        }
    }

    private double getRandomOffset(RandomSource random) {
        return random.nextInt(2) == 0 ? 0.18D : -0.18D;
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos pos, Entity entity) {
        if (blockState.getValue(BURNING)) {
            VoxelShape blockShape = blockState.getShape(level, pos);
            AABB aabb = entity.getBoundingBox().move(-pos.getX(), -pos.getY(), -pos.getZ()).inflate(0.01D);
            if (!blockShape.isEmpty() && Shapes.joinIsNotEmpty(blockShape, Shapes.create(aabb), BooleanOp.AND)) {
                entity.hurt(level.damageSources().hotFloor(), 1.0F);
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
        return CableHelper.getCableShape(blockState);
    }

    @Override
    protected void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(blockState, level, pos, oldState, movedByPiston);
        if (!level.isClientSide()) {
            level.getBlockEntity(pos, ModBlockEntities.COPPER_CABLE.get()).ifPresent(CopperCableBlockEntity::updateConnections);
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        this.updateFluid(blockState, level, pos);
        return this.updateBlockState(level, blockState, pos);
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
                shouldConnect = blockEntity != null && ((Level)level).getCapability(Capabilities.EnergyStorage.BLOCK, relativePos, direction.getOpposite()) != null;
            }
            newState = newState.setValue(CONNECTIONS.get(direction), shouldConnect);
        }
        return newState;
    }

    @Override
    protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return !blockState.getValue(BURNING);
    }

    @Override
    public @Nullable PathType getBlockPathType(BlockState blockState, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return blockState.getValue(BURNING) ? PathType.DAMAGE_FIRE : PathType.WALKABLE;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = this.updateBlockState(context.getLevel(), this.defaultBlockState(), context.getClickedPos());
        return blockState.setValue(WATERLOGGED, this.shouldWaterlogOnPlacement(context));
    }

    public FluidState getFluidState(BlockState blockState) {
        return this.getFluidForBlockState(blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DOWN, UP, EAST, WEST, NORTH, SOUTH, WATERLOGGED, BURNING);
    }

}
