package com.mikitellurium.turtlecharginstation.block;

import com.mikitellurium.turtlecharginstation.registry.ModBlockEntities;
import com.mikitellurium.turtlecharginstation.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlecharginstation.networking.ModMessages;
import com.mikitellurium.turtlecharginstation.networking.packets.EnergySyncS2CPacket;
import com.mikitellurium.turtlecharginstation.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TurtleChargingStationBlock extends BaseEntityBlock {

    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
    public static final BooleanProperty CHARGING = BooleanProperty.create("charging");

    public TurtleChargingStationBlock(BlockBehaviour.Properties properties) {
        super(properties
                .mapColor(MapColor.COLOR_BLACK)
                .requiresCorrectToolForDrops()
                .strength(3.0F, 6.0F)
                .sound(SoundType.METAL));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ENABLED, Boolean.TRUE)
                .setValue(CHARGING, Boolean.FALSE));
    }

    // Block entity stuff
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
        return new TurtleChargingStationBlockEntity(pos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    // Functionality
    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand interactionHand,
                                 BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof TurtleChargingStationBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) player, (TurtleChargingStationBlockEntity)entity, pos);
                ModMessages.sendToClients(
                        new EnergySyncS2CPacket(((TurtleChargingStationBlockEntity) entity).getEnergyStorage().getEnergyStored(), pos));
            } else {
                throw new IllegalStateException("Container provider is missing");
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.TURTLE_CHARGING_STATION.get(),
                (tickLevel, pos, state, blockEntity) -> blockEntity.tick(tickLevel, pos, state));
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(blockState.getBlock())) {
            this.checkPoweredState(level, pos, blockState);
        }
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        this.checkPoweredState(level, pos, blockState);
    }

    private void checkPoweredState(Level level, BlockPos pos, BlockState blockState) {
        boolean flag = !level.hasNeighborSignal(pos);
        if (flag != blockState.getValue(ENABLED)) {
            level.setBlock(pos, blockState.setValue(ENABLED, flag), 2);
        }
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof TurtleChargingStationBlockEntity) {
                ((TurtleChargingStationBlockEntity)blockentity).setCustomName(itemStack.getHoverName());
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootParams.Builder lootParams) {
        List<ItemStack> drops = super.getDrops(blockState, lootParams);
        for (ItemStack itemStack : drops) {
            if (itemStack.is(ModBlocks.TURTLE_CHARGING_STATION.get().asItem())) {
                BlockEntity be = lootParams.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
                if (be instanceof TurtleChargingStationBlockEntity station && station.hasCustomName()) {
                    itemStack.setHoverName(station.getDisplayName());
                }
            }
        }
        return drops;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ENABLED);
        builder.add(CHARGING);
    }

}
