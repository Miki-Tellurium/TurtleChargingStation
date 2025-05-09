package com.mikitellurium.turtlecharginstation.registry;

import com.mikitellurium.telluriumforge.registry.BlockRegistrator;
import com.mikitellurium.turtlecharginstation.common.block.CopperCableBlock;
import com.mikitellurium.turtlecharginstation.common.block.ThunderchargeDynamoBlock;
import com.mikitellurium.turtlecharginstation.common.block.TurtleChargingStationBlock;
import com.mikitellurium.turtlecharginstation.util.FastLoc;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    public static BlockRegistrator REGISTRATOR;
    public static final RegistryObject<Block> TURTLE_CHARGING_STATION;
    public static final RegistryObject<Block> THUNDERCHARGE_DYNAMO;
    public static final RegistryObject<Block> COPPER_CABLE;

    static {
        REGISTRATOR = BlockRegistrator.makeRegistrator(ModItems.REGISTRATOR, FastLoc.modId());
        TURTLE_CHARGING_STATION = REGISTRATOR.registerWithItem("turtle_charging_station", () -> new TurtleChargingStationBlock(BlockBehaviour.Properties.of()));
        THUNDERCHARGE_DYNAMO = REGISTRATOR.registerWithItem("thundercharge_dynamo", () -> new ThunderchargeDynamoBlock(BlockBehaviour.Properties.of()));
        COPPER_CABLE = REGISTRATOR.registerWithItem("copper_cable", () -> new CopperCableBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    }
}
