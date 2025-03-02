package com.mikitellurium.turtlecharginstation.registry;

import com.mikitellurium.telluriumforge.registry.BlockWithItemRegistrator;
import com.mikitellurium.turtlecharginstation.block.ThunderchargeDynamoBlock;
import com.mikitellurium.turtlecharginstation.block.TurtleChargingStationBlock;
import com.mikitellurium.turtlecharginstation.util.FastLoc;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {

    public static BlockWithItemRegistrator REGISTRY;
    public static final RegistryObject<Block> TURTLE_CHARGING_STATION_BLOCK;
    public static final RegistryObject<Block> THUNDERCHARGE_DYNAMO_BLOCK;

    static {
        REGISTRY = BlockWithItemRegistrator.makeRegistrator(ModItems.REGISTRY, FastLoc.modId());
        TURTLE_CHARGING_STATION_BLOCK = REGISTRY.registerWithItem("turtle_charging_station", TurtleChargingStationBlock::new);
        THUNDERCHARGE_DYNAMO_BLOCK = REGISTRY.registerWithItem("tundercharge_dynamo", ThunderchargeDynamoBlock::new);
    }

}
