package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.BlockRegistrator;
import com.mikitellurium.turtlechargingstation.common.block.TurtleChargingStationBlock;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModBlocks {
    public static final BlockRegistrator REGISTRATOR;
    public static final Block TURTLE_CHARGING_STATION;
//    public static final Block THUNDERCHARGE_DYNAMO;
//    public static final Block COPPER_CABLE;

    static {
        REGISTRATOR = BlockRegistrator.makeRegistrator(FastLoc.modId());
        TURTLE_CHARGING_STATION = REGISTRATOR.registerWithItem("turtle_charging_station", new TurtleChargingStationBlock());
        //        THUNDERCHARGE_DYNAMO = REGISTRATOR.registerWithItem("thundercharge_dynamo", () -> new ThunderchargeDynamoBlock(BlockBehaviour.Properties.of()));
//        COPPER_CABLE = REGISTRATOR.registerWithItem("copper_cable", () -> new CopperCableBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    }
}
