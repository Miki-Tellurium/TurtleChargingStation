package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.BlockRegistrator;
import com.mikitellurium.turtlechargingstation.common.block.EnergyTestBlock;
import com.mikitellurium.turtlechargingstation.common.block.TurtleChargingStationBlock;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.minecraft.block.Block;

public class ModBlocks {
    public static final BlockRegistrator REGISTRATOR;
    public static final Block TURTLE_CHARGING_STATION;

    static {
        REGISTRATOR = BlockRegistrator.makeRegistrator(FastLoc.modId());
        TURTLE_CHARGING_STATION = REGISTRATOR.registerWithItem("turtle_charging_station", new TurtleChargingStationBlock());
        REGISTRATOR.registerWithItem("energy_test", new EnergyTestBlock());
    }
}
