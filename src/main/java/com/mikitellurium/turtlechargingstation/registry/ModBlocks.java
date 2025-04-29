package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.BlockRegistrator;
import com.mikitellurium.turtlechargingstation.common.block.CopperCableBlock;
import com.mikitellurium.turtlechargingstation.common.block.ThunderchargeDynamoBlock;
import com.mikitellurium.turtlechargingstation.common.block.TurtleChargingStationBlock;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

public final class ModBlocks {

    public static BlockRegistrator REGISTRY;
    public static final DeferredBlock<Block> TURTLE_CHARGING_STATION;
    public static final DeferredBlock<Block> THUNDERCHARGE_DYNAMO;
    public static final DeferredBlock<Block> COPPER_CABLE;

    static {
        REGISTRY = BlockRegistrator.makeRegistrator(ModItems.REGISTRY, FastLoc.modId());
        TURTLE_CHARGING_STATION = REGISTRY.registerWithItem("turtle_charging_station", () -> new TurtleChargingStationBlock(BlockBehaviour.Properties.of()));
        THUNDERCHARGE_DYNAMO = REGISTRY.registerWithItem("thundercharge_dynamo", () -> new ThunderchargeDynamoBlock(BlockBehaviour.Properties.of()));
        COPPER_CABLE = REGISTRY.registerWithItem("copper_cable", () -> new CopperCableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK)));
    }

}
