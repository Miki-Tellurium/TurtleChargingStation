package com.mikitellurium.turtlecharginstation.registry;

import com.mikitellurium.telluriumforge.registry.BlockWithItemRegistrator;
import com.mikitellurium.turtlecharginstation.block.ThunderchargeDynamoBlock;
import com.mikitellurium.turtlecharginstation.block.TurtleChargingStationBlock;
import com.mikitellurium.turtlecharginstation.util.FastLoc;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {

    public static BlockWithItemRegistrator REGISTRY;
    public static final RegistryObject<Block> TURTLE_CHARGING_STATION;
    public static final RegistryObject<Block> THUNDERCHARGE_DYNAMO;
    public static final RegistryObject<Block> COPPER_CABLE;

    static {
        REGISTRY = BlockWithItemRegistrator.makeRegistrator(ModItems.REGISTRY, FastLoc.modId());
        TURTLE_CHARGING_STATION = REGISTRY.registerWithItem("turtle_charging_station", () -> new TurtleChargingStationBlock(BlockBehaviour.Properties.of()));
        THUNDERCHARGE_DYNAMO = REGISTRY.registerWithItem("thundercharge_dynamo", () -> new ThunderchargeDynamoBlock(BlockBehaviour.Properties.of()));
    }

}
