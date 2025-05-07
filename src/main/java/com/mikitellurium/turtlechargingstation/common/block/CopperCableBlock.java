package com.mikitellurium.turtlechargingstation.common.block;

import com.mikitellurium.telluriumforge.block.WaterloggedHelper;
import com.mikitellurium.telluriumforge.blockentity.TickingBlockEntity;
import com.mikitellurium.turtlechargingstation.common.blockentity.CopperCableBlockEntity;
import com.mikitellurium.turtlechargingstation.registry.ModBlockEntities;
import com.mikitellurium.turtlechargingstation.registry.ModBlocks;
import com.mikitellurium.turtlechargingstation.util.CableHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Util;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

import java.util.EnumMap;
import java.util.Map;

public class CopperCableBlock extends BlockWithEntity implements WaterloggedHelper {
    public static final BooleanProperty BURNING = BooleanProperty.of("burning");
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    public static final BooleanProperty UP = BooleanProperty.of("up");
    public static final BooleanProperty DOWN = BooleanProperty.of("down");
    public static final Map<Direction, BooleanProperty> CONNECTIONS = Util.make(new EnumMap<>(Direction.class), (map) -> {
        map.put(Direction.DOWN, DOWN);
        map.put(Direction.UP, UP);
        map.put(Direction.EAST, EAST);
        map.put(Direction.WEST, WEST);
        map.put(Direction.NORTH, NORTH);
        map.put(Direction.SOUTH, SOUTH);
    });

    public CopperCableBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState()
                .with(BURNING, false)
                .with(DOWN, false)
                .with(UP, false)
                .with(EAST, false)
                .with(WEST, false)
                .with(NORTH, false)
                .with(SOUTH, false)
                .with(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(CopperCableBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState blockState) {
        return new CopperCableBlockEntity(pos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState blockState, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.COPPER_CABLE, TickingBlockEntity.getTicker());
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void randomDisplayTick(BlockState blockState, World world, BlockPos pos, Random random) {
        if (blockState.get(BURNING) && random.nextFloat() < 0.7F) {
            Direction direction = Direction.random(random);
            if (blockState.get(CONNECTIONS.get(direction))) {
                Vec3d vec3 = pos.toCenterPos();
                double d0 = direction.getOffsetX() == 0 ? getRandomOffset(random) : direction.getOffsetX() * (random.nextDouble() / 2);
                double d1 = direction.getOffsetY() == 0 ? getRandomOffset(random) : direction.getOffsetY() * (random.nextDouble() / 2);
                double d2 = direction.getOffsetZ() == 0 ? getRandomOffset(random) : direction.getOffsetZ() * (random.nextDouble() / 2);
                ParticleEffect particleType = blockState.get(WATERLOGGED) ? ParticleTypes.BUBBLE : ParticleTypes.SMOKE;
                double ySpeed = blockState.get(WATERLOGGED) ? 0.2D : -0.01D;
                world.addParticle(particleType, vec3.x + d0, vec3.y + d1, vec3.z + d2, 0, ySpeed, 0);
            }
        }
        super.randomDisplayTick(blockState, world, pos, random);
    }

    private double getRandomOffset(Random random) {
        return random.nextInt(2) == 0 ? 0.18D : -0.18D;
    }

    @Override
    public void onEntityCollision(BlockState blockState, World world, BlockPos pos, Entity entity) {
        if (blockState.get(BURNING)) {
            VoxelShape blockShape = blockState.getCollisionShape(world, pos);
            Box aabb = entity.getBoundingBox().offset(-pos.getX(), -pos.getY(), -pos.getZ()).expand(0.01D);
            if (!blockShape.isEmpty() && VoxelShapes.matchesAnywhere(blockShape, VoxelShapes.cuboid(aabb), BooleanBiFunction.AND)) {
                entity.damage(world.getDamageSources().hotFloor(), 1.0F);
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockView world, BlockPos pos, ShapeContext context) {
        return CableHelper.getCableShape(blockState);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getCollisionShape(blockState, world, pos, context);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState blockState, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        this.updateFluid(blockState, world, pos);
        return this.updateBlockState(world, blockState, pos);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        if (!world.isClient) {
           world.getBlockEntity(pos, ModBlockEntities.COPPER_CABLE).ifPresent(CopperCableBlockEntity::updateConnections);
        }
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState blockState, PlayerEntity player) {
        blockState.updateNeighbors(world, pos, 3);
        if (!world.isClient) {
            world.getBlockEntity(pos, ModBlockEntities.COPPER_CABLE).ifPresent(CopperCableBlockEntity::remove);
        }
        return super.onBreak(world, pos, blockState, player);
    }

    private BlockState updateBlockState(WorldAccess world, BlockState blockState, BlockPos pos) {
        BlockState newState = blockState;
        for (Direction direction : Direction.values()) {
            boolean shouldConnect;
            BlockPos relativePos = pos.offset(direction);
            BlockState neighbourState = world.getBlockState(relativePos);
            if (neighbourState.isOf(ModBlocks.COPPER_CABLE)) {
                shouldConnect = true;
            } else {
                BlockEntity blockEntity = world.getBlockEntity(relativePos);
                shouldConnect = blockEntity != null && EnergyStorage.SIDED.find((World) world, blockEntity.getPos(), blockEntity.getCachedState(), blockEntity, direction.getOpposite()) != null;
            }
            newState = newState.with(CONNECTIONS.get(direction), shouldConnect);
        }
        return newState;
    }

    @Override
    protected boolean canPathfindThrough(BlockState blockState, NavigationType type) {
        return !blockState.get(BURNING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState blockState = this.updateBlockState(context.getWorld(), this.getDefaultState(), context.getBlockPos());
        return blockState.with(WATERLOGGED, this.shouldWaterlogOnPlacement(context));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(DOWN, UP, EAST, WEST, NORTH, SOUTH, WATERLOGGED, BURNING);
    }
}
