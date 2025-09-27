package com.mikitellurium.turtlechargingstation.common.block;

import com.mikitellurium.telluriumforge.blockentity.TickingBlockEntity;
import com.mikitellurium.turtlechargingstation.registry.ModBlockEntities;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlechargingstation.networking.packets.EnergySyncPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class TurtleChargingStationBlock extends BlockWithEntity {

    public static final BooleanProperty ENABLED = Properties.ENABLED;
    public static final BooleanProperty CHARGING = BooleanProperty.of("charging");

    public TurtleChargingStationBlock(Settings settings) {
        super(settings
                .mapColor(MapColor.BLACK)
                .requiresTool()
                .strength(3.0F, 6.0F)
                .sounds(BlockSoundGroup.METAL));
        setDefaultState(getDefaultState()
                .with(ENABLED, true)
                .with(CHARGING, false));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TurtleChargingStationBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState blockState, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.TURTLE_CHARGING_STATION, TickingBlockEntity.getTicker());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
                              Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof TurtleChargingStationBlockEntity stationBlockEntity) {
                NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
                if (screenHandlerFactory != null) {
                    player.openHandledScreen(screenHandlerFactory);
                }
                ServerPlayNetworking.send((ServerPlayerEntity) player,
                        new EnergySyncPacket(stationBlockEntity.getPos(), stationBlockEntity.getEnergy()));
            } else {
                throw new IllegalStateException("Container provider is missing");
            }
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof TurtleChargingStationBlockEntity) {
                ((TurtleChargingStationBlockEntity)blockEntity).setCustomName(itemStack.getName());
            }
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(state.getBlock())) {
            checkPoweredState(world, pos, state);
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock,
                               BlockPos sourcePos, boolean notify) {
        checkPoweredState(world, pos, state);
    }

    private void checkPoweredState(World world, BlockPos pos, BlockState state) {
        boolean flag = !world.isReceivingRedstonePower(pos);
        if (flag != state.get(ENABLED)) {
            world.setBlockState(pos, state.with(ENABLED, flag), 2);
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (world.isClient) return;

        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockentity = world.getBlockEntity(pos);
            if (blockentity instanceof TurtleChargingStationBlockEntity turtleChargingStation) {
                ItemScatterer.spawn(world, pos, turtleChargingStation.getInventory());
                world.updateComparators(pos, this);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ENABLED);
        builder.add(CHARGING);
    }

}
