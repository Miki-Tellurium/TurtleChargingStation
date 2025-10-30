package com.mikitellurium.turtlechargingstation;

import com.mikitellurium.turtlechargingstation.client.ModGuiHandler;
import com.mikitellurium.turtlechargingstation.registry.ModRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = TurtleChargingStationMod.MOD_ID, name = TurtleChargingStationMod.NAME, version = TurtleChargingStationMod.VERSION)
public class TurtleChargingStationMod {
    public static final String MOD_ID = "turtlechargingstation";
    public static final String NAME = "Turtle Charging Station";
    public static final String VERSION = "1.7.1-beta";
    private static final TurtleChargingStationMod INSTANCE = new TurtleChargingStationMod();
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private TurtleChargingStationMod() {}

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModRegistries.register();
        CommonSetup.register();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, ModGuiHandler.INSTANCE);
    }

    @Mod.InstanceFactory
    public static TurtleChargingStationMod getInstance() {
        return INSTANCE;
    }

    public static String modId() {
        return MOD_ID;
    }

    public static Logger logger() {
        return LOGGER;
    }
}
