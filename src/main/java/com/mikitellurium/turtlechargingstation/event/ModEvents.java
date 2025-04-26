package com.mikitellurium.turtlechargingstation.event;

import com.mikitellurium.turtlechargingstation.TurtleChargingStationMod;
import com.mikitellurium.turtlechargingstation.blockentity.ThunderchargeDynamoBlockEntity;
import com.mikitellurium.turtlechargingstation.mixin.CreeperAccessor;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.List;
import java.util.Optional;


@EventBusSubscriber(modid = TurtleChargingStationMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

    @SubscribeEvent
    public static void onLightningStrike(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) {
            return;
        }
        if (event.getEntity() instanceof LightningBolt lightningBolt) {
            BlockEntity maybeDynamo = getBlockEntityBelowStrike(lightningBolt.level(), lightningBolt.getOnPos());
            if (maybeDynamo == null) return;
            if (maybeDynamo instanceof ThunderchargeDynamoBlockEntity dynamo) {
                dynamo.recharge();
                if (lightningBolt.getCause() == null) {
                    maybeDoSpawnCreeper((ServerLevel) lightningBolt.level(), dynamo.getBlockPos());
                }
            }
        }
    }

    private static BlockEntity getBlockEntityBelowStrike(Level level, BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        return blockEntity != null ? blockEntity : level.getBlockEntity(blockPos.below());
    }

    @SuppressWarnings("ConstantConditions")
    private static void maybeDoSpawnCreeper(ServerLevel level, BlockPos pos) {
        if (level.random.nextInt(1023) == 0) {
            Optional<BlockPos> blockPos = getPossibleSpawnPos(level, pos);
            blockPos.ifPresent((p) -> {
                Creeper creeper = EntityType.CREEPER.spawn(level, p, MobSpawnType.EVENT);
                if (creeper != null) {
                    EntityDataAccessor<Boolean> creeperIsCharged = CreeperAccessor.getDATA_IS_POWERED();
                    creeper.getEntityData().set(creeperIsCharged, true);
                }
            });
        }
    }

    private static Optional<BlockPos> getPossibleSpawnPos(Level level, BlockPos blockPos) {
        BlockPos startPos = blockPos.offset(1, 2, 1);
        BlockPos downPos = blockPos.offset(-1, -10, -1);
        SpawnPlacementType placementType = SpawnPlacements.getPlacementType(EntityType.CREEPER);

        List<BlockPos> list = BlockPos.betweenClosedStream(startPos, downPos)
                .filter((pos) -> level.canSeeSky(pos) && placementType.isSpawnPositionOk(level, pos, EntityType.CREEPER))
                .map(BlockPos::immutable)
                .toList();
        return Util.getRandomSafe(list, level.random);
    }

}
