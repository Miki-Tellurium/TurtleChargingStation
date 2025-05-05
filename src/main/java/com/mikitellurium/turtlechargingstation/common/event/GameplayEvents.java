package com.mikitellurium.turtlechargingstation.common.event;

import com.mikitellurium.telluriumforge.event.EventHelper;
import com.mikitellurium.turtlechargingstation.common.blockentity.ThunderchargeDynamoBlockEntity;
import com.mikitellurium.turtlechargingstation.mixin.CreeperEntityAccessor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class GameplayEvents {

    private static final EventHelper HELPER = new EventHelper();

    public static void register() {
        HELPER
                .addListener(ServerEntityEvents.ENTITY_LOAD, GameplayEvents::onLightningStrike)
                .registerAll();
    }

    private static void onLightningStrike(Entity entity, ServerWorld world) {
        if (entity instanceof LightningEntity lightningBolt) {
            BlockEntity blockEntity = getBlockEntityBelow(world, lightningBolt.getSteppingPos());
            if (blockEntity == null) return;
            if (blockEntity instanceof ThunderchargeDynamoBlockEntity dynamo) {
                dynamo.recharge();
                if (lightningBolt.getChanneler() == null) {
                    maybeDoSpawnCreeper((ServerWorld) lightningBolt.getWorld(), dynamo.getPos());
                }
            }
        }
    }

    private static BlockEntity getBlockEntityBelow(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity != null ? blockEntity : world.getBlockEntity(pos.down());
    }

    private static void maybeDoSpawnCreeper(ServerWorld world, BlockPos pos) {
        if (world.random.nextInt(1023) == 0) {
            Optional<BlockPos> blockPos = getPossibleSpawnPos(world, pos);
            blockPos.ifPresent((p) -> {
                CreeperEntity creeper = EntityType.CREEPER.spawn(world, p, SpawnReason.EVENT);
                if (creeper != null) {
                    TrackedData<Boolean> creeperIsCharged = CreeperEntityAccessor.getCHARGED();
                    creeper.getDataTracker().set(creeperIsCharged, true);
                }
            });
        }
    }

    private static Optional<BlockPos> getPossibleSpawnPos(World world, BlockPos blockPos) {
        BlockPos startPos = blockPos.add(1, 2, 1);
        BlockPos downPos = blockPos.add(-1, -10, -1);
        List<BlockPos> list = BlockPos.stream(startPos, downPos)
                .filter((pos) -> world.isSkyVisible(pos) && SpawnHelper.canSpawn(SpawnRestriction.Location.ON_GROUND, world, pos, EntityType.CREEPER))
                .map(BlockPos::toImmutable)
                .toList();
        return Util.getRandomOrEmpty(list, world.random);
    }

}
