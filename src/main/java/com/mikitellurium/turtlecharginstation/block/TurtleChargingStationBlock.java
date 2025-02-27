package com.mikitellurium.turtlecharginstation.block;

import com.mikitellurium.turtlecharginstation.registry.ModBlockEntities;
import com.mikitellurium.turtlecharginstation.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlecharginstation.networking.ModMessages;
import com.mikitellurium.turtlecharginstation.networking.packets.EnergySyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class TurtleChargingStationBlock extends BaseEntityBlock {

    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
    public static final BooleanProperty CHARGING = BooleanProperty.create("charging");

    public TurtleChargingStationBlock() {
        super(Properties.of()
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
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TurtleChargingStationBlockEntity(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    // Functionality
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
                                 BlockHitResult pHit) {
        if (!pLevel.isClientSide) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof TurtleChargingStationBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) pPlayer, (TurtleChargingStationBlockEntity)entity, pPos);
                ModMessages.sendToClients(
                        new EnergySyncS2CPacket(((TurtleChargingStationBlockEntity) entity).getEnergyStorage().getEnergyStored(), pPos));
            } else {
                throw new IllegalStateException("Container provider is missing");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (!pOldState.is(pState.getBlock())) {
            checkPoweredState(pLevel, pPos, pState);
        }
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        this.checkPoweredState(pLevel, pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker getTicker(Level pLevel, BlockState pState,
                                                               BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.TURTLE_CHARGING_STATION.get(),
                TurtleChargingStationBlockEntity::tick);
    }

    private void checkPoweredState(Level level, BlockPos pos, BlockState state) {
        boolean flag = !level.hasNeighborSignal(pos);
        if (flag != state.getValue(ENABLED)) {
            level.setBlock(pos, state.setValue(ENABLED, Boolean.valueOf(flag)), 2);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ENABLED);
        pBuilder.add(CHARGING);
    }

}
