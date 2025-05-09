package com.mikitellurium.turtlecharginstation.common.event;

import com.mikitellurium.telluriumforge.event.EventHelper;
import com.mikitellurium.turtlecharginstation.TurtleChargingStationMod;
import com.mikitellurium.turtlecharginstation.common.blockentity.ThunderchargeDynamoBlockEntity;
import com.mikitellurium.turtlecharginstation.common.integration.computercraft.ThunderchargeDynamoPeripheral;
import com.mikitellurium.turtlecharginstation.common.integration.computercraft.TurtleChargingStationPeripheral;
import com.mikitellurium.turtlecharginstation.datagen.DataGenerators;
import com.mikitellurium.turtlecharginstation.mixin.CreeperAccessor;
import com.mikitellurium.turtlecharginstation.registry.ModCreativeTab;
import com.mikitellurium.turtlecharginstation.registry.ModTags;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public class ModEvents {
    public static void register(IEventBus modEventBus) {
        final EventHelper HELPER = new EventHelper();
        HELPER
                .addListener(modEventBus, ModCreativeTab::buildCreativeTab)
                .addListener(modEventBus, DataGenerators::gatherData)
                .addListener(MinecraftForge.EVENT_BUS, ModEvents::onLightningStrike)
                .registerClass(MinecraftForge.EVENT_BUS, ThunderchargeDynamoPeripheral.class)
                .registerAll();
    }

    public static void onLightningStrike(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity.getType().is(ModTags.DYNAMO_ACTIVATORS)) {
            BlockEntity maybeDynamo = getBlockEntityBelowStrike(entity.level(), entity.getOnPos());
            if (maybeDynamo == null) return;
            if (maybeDynamo instanceof ThunderchargeDynamoBlockEntity dynamo) {
                dynamo.recharge();
                if (entity instanceof LightningBolt lightningBolt && lightningBolt.getCause() == null) {
                   maybeDoSpawnCreeper((ServerLevel) entity.level(), dynamo.getBlockPos());
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

        List<BlockPos> list = BlockPos.betweenClosedStream(startPos, downPos)
                .filter((pos) -> level.canSeeSky(pos) && NaturalSpawner.canSpawnAtBody(SpawnPlacements.Type.NO_RESTRICTIONS, level, pos, EntityType.CREEPER))
                .map(BlockPos::immutable)
                .toList();
        return Util.getRandomSafe(list, level.random);
    }
}
