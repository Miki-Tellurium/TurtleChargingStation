package com.mikitellurium.turtlechargingstation.common.block;

import com.mikitellurium.telluriumforge.blockentity.TickingBlockEntity;
import com.mikitellurium.turtlechargingstation.registry.ModBlockEntities;
import com.mikitellurium.turtlechargingstation.common.blockentity.ThunderchargeDynamoBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ThunderchargeDynamoBlock extends BlockWithEntity {

    public static BooleanProperty POWERED = Properties.POWERED;

    public ThunderchargeDynamoBlock(Settings settings) {
        super(settings
                .mapColor(MapColor.IRON_GRAY)
                .requiresTool()
                .strength(3.0F, 6.0F)
                .sounds(BlockSoundGroup.METAL));
        setDefaultState(getDefaultState().with(POWERED, false));
    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ThunderchargeDynamoBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState blockState, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.THUNDERCHARGE_DYNAMO, TickingBlockEntity.getTicker());
    }

    @Override
    public void randomDisplayTick(BlockState blockState, World world, BlockPos blockPos, Random random) {
        if (blockState.get(POWERED)) {
            if (random.nextInt(4) == 0) {
                Direction direction = Direction.random(random);
                if (!(direction == Direction.UP || direction == Direction.DOWN)) {
                    BlockPos relativePos = blockPos.offset(direction);
                    BlockState adjacentBlock = world.getBlockState(relativePos);
                    if (!adjacentBlock.isOpaque() || !adjacentBlock.isSideSolidFullSquare(world, relativePos, direction.getOpposite())) {
                        double d0 = direction.getOffsetX() == 0 ? random.nextDouble() : 0.5D + (double)direction.getOffsetX() * 0.54D;
                        double d1 = direction.getOffsetY() == 0 ? random.nextDouble() : 0.5D + (double)direction.getOffsetY() * 0.54D;
                        double d2 = direction.getOffsetZ() == 0 ? random.nextDouble() : 0.5D + (double)direction.getOffsetZ() * 0.54D;
                        world.addParticle(ParticleTypes.FIREWORK,
                                blockPos.getX() + d0, blockPos.getY() + d1, blockPos.getZ() + d2,
                                0.0D, random.nextDouble() * 0.1D, 0.0D);
                    }
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.turtlechargingstation.thundercharge_dynamo"));
        } else {
            tooltip.add(Text.translatable("tooltip.turtlechargingstation.thundercharge_dynamo.shift")
                    .formatted(Formatting.AQUA));
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

}
