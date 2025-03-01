package com.mikitellurium.turtlecharginstation.registry;

import com.mikitellurium.turtlecharginstation.blockentity.ThunderchargeDynamoBlockEntity;
import com.mikitellurium.turtlecharginstation.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlecharginstation.util.FastLoc;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FastLoc.modId());

    public static final RegistryObject<BlockEntityType<TurtleChargingStationBlockEntity>> TURTLE_CHARGING_STATION =
            BLOCK_ENTITIES.register("turtle_charging_station", () -> BlockEntityType.Builder.of(TurtleChargingStationBlockEntity::new,
                    ModBlocks.TURTLE_CHARGING_STATION_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<ThunderchargeDynamoBlockEntity>> THUNDERCHARGE_DYNAMO =
            BLOCK_ENTITIES.register("thundercharge_dynamo", () -> BlockEntityType.Builder.of(ThunderchargeDynamoBlockEntity::new,
                    ModBlocks.THUNDERCHARGE_DYNAMO_BLOCK.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
