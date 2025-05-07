package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.BlockRegistrator;
import com.mikitellurium.turtlechargingstation.common.block.CopperCableBlock;
import com.mikitellurium.turtlechargingstation.common.block.ThunderchargeDynamoBlock;
import com.mikitellurium.turtlechargingstation.common.block.TurtleChargingStationBlock;
import com.mikitellurium.turtlechargingstation.test.EnergyTestBlock;
import com.mikitellurium.turtlechargingstation.util.FastId;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class ModBlocks {
    public static final BlockRegistrator REGISTRATOR;
    public static final Block TURTLE_CHARGING_STATION;
    public static final Block THUNDERCHARGE_DYNAMO;
    public static final Block COPPER_CABLE;

    static  {
        REGISTRATOR = new BlockRegistrator(FastId.modId());
        TURTLE_CHARGING_STATION = REGISTRATOR.registerWithItem("turtle_charging_station", () -> new TurtleChargingStationBlock(FabricBlockSettings.create()));
        THUNDERCHARGE_DYNAMO = REGISTRATOR.registerWithItem("thundercharge_dynamo", () -> new ThunderchargeDynamoBlock(FabricBlockSettings.create()));
        COPPER_CABLE = REGISTRATOR.registerWithItem("copper_cable", () -> new CopperCableBlock(FabricBlockSettings.copyOf(Blocks.COPPER_BLOCK)));
    }
}
