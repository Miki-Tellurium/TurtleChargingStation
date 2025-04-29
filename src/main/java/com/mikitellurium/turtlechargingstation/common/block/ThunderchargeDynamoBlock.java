package com.mikitellurium.turtlechargingstation.common.block;

import com.mikitellurium.turtlechargingstation.common.blockentity.ThunderchargeDynamoBlockEntity;
import com.mikitellurium.turtlechargingstation.registry.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ThunderchargeDynamoBlock extends BaseEntityBlock {

    public static BooleanProperty POWERED = BlockStateProperties.POWERED;

    public ThunderchargeDynamoBlock(Properties properties) {
        super(properties
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(3.0F, 6.0F)
                .sound(SoundType.METAL));
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(POWERED, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(ThunderchargeDynamoBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ThunderchargeDynamoBlockEntity(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.THUNDERCHARGE_DYNAMO.get(),
                (tickLevel, pos, state, blockEntity) -> blockEntity.tick(tickLevel, pos, state));
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource random) {
        if (blockState.getValue(POWERED)) {
            if (random.nextInt(4) == 0) {
                Direction direction = Direction.getRandom(random);
                if (!(direction == Direction.UP || direction == Direction.DOWN)) {
                    BlockPos relativePos = blockPos.relative(direction);
                    BlockState adjacentBlock = level.getBlockState(relativePos);
                    if (!adjacentBlock.canOcclude() || !adjacentBlock.isFaceSturdy(level, relativePos, direction.getOpposite())) {
                        double d0 = direction.getStepX() == 0 ? random.nextDouble() : 0.5D + (double)direction.getStepX() * 0.54D;
                        double d1 = direction.getStepY() == 0 ? random.nextDouble() : 0.5D + (double)direction.getStepY() * 0.54D;
                        double d2 = direction.getStepZ() == 0 ? random.nextDouble() : 0.5D + (double)direction.getStepZ() * 0.54D;
                        level.addParticle(ParticleTypes.FIREWORK,
                                blockPos.getX() + d0, blockPos.getY() + d1, blockPos.getZ() + d2,
                                0.0D, random.nextDouble() * 0.1D, 0.0D);
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("tooltip.turtlechargingstation.thundercharge_dynamo"));
        } else {
            components.add(Component.translatable("tooltip.turtlechargingstation.thundercharge_dynamo.shift").withStyle(ChatFormatting.AQUA));
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED);
    }

}
