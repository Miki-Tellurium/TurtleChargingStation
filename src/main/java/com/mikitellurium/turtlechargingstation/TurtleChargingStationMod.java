package com.mikitellurium.turtlechargingstation;

import com.mikitellurium.turtlechargingstation.registry.ModRegistries;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = TurtleChargingStationMod.MOD_ID, name = TurtleChargingStationMod.NAME, version = TurtleChargingStationMod.VERSION)
public class TurtleChargingStationMod {
    public static final String MOD_ID = "turtlechargingstation";
    public static final String NAME = "Turtle Charging Station";
    public static final String VERSION = "1.7.1-beta";
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Mod.InstanceFactory
    private static TurtleChargingStationMod constructor() {
        return new TurtleChargingStationMod();
    }

    private TurtleChargingStationMod() {
        ModRegistries.register();
//        CommonSetup.register(modEventBus);
//        GameplayEvents.register(modEventBus);
//        Networking.register();
//        ModConfigs.register();
    }

    public static String modId() {
        return MOD_ID;
    }

    public static Logger logger() {
        return LOGGER;
    }
}
