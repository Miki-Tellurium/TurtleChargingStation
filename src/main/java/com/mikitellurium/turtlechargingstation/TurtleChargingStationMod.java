package com.mikitellurium.turtlechargingstation;

import com.mikitellurium.turtlechargingstation.common.event.GameplayEvents;
import com.mikitellurium.turtlechargingstation.config.ModConfigs;
import com.mikitellurium.turtlechargingstation.registry.ModRegistries;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(TurtleChargingStationMod.MOD_ID)
public class TurtleChargingStationMod {

    public static final String MOD_ID = "turtlechargingstation";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TurtleChargingStationMod(ModContainer modContainer) {
        IEventBus modEventBus = modContainer.getEventBus();
        ModRegistries.register();
        CommonSetup.register(modEventBus);
        GameplayEvents.register(modEventBus);
        ModConfigs.register();
    }

    public static String modId() {
        return MOD_ID;
    }

    public static Logger logger() {
        return LOGGER;
    }

}
