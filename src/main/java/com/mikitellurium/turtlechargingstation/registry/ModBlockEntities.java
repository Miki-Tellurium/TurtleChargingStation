package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.turtlechargingstation.TurtleChargingStationMod;
import com.mikitellurium.turtlechargingstation.blockentity.ThunderchargeDynamoBlockEntity;
import com.mikitellurium.turtlechargingstation.blockentity.TurtleChargingStationBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TurtleChargingStationMod.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurtleChargingStationBlockEntity>> TURTLE_CHARGING_STATION =
            BLOCK_ENTITIES.register("turtle_charging_station", () -> BlockEntityType.Builder.of(TurtleChargingStationBlockEntity::new,
                    ModBlocks.TURTLE_CHARGING_STATION_BLOCK.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ThunderchargeDynamoBlockEntity>> THUNDERCHARGE_DYNAMO =
            BLOCK_ENTITIES.register("thundercharge_dynamo", () -> BlockEntityType.Builder.of(ThunderchargeDynamoBlockEntity::new,
                    ModBlocks.THUNDERCHARGE_DYNAMO_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
