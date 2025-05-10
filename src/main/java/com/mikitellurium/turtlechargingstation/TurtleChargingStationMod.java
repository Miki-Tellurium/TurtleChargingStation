package com.mikitellurium.turtlechargingstation;

import com.mikitellurium.turtlechargingstation.common.event.GameplayEvents;
import com.mikitellurium.turtlechargingstation.config.ModConfigs;
import com.mikitellurium.turtlechargingstation.networking.Networking;
import com.mikitellurium.turtlechargingstation.registry.ModRegistries;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(TurtleChargingStationMod.MOD_ID)
public class TurtleChargingStationMod {

    public static final String MOD_ID = "turtlechargingstation";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TurtleChargingStationMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModRegistries.register();
        CommonSetup.register(modEventBus);
        GameplayEvents.register(modEventBus);
        Networking.register();
        ModConfigs.register();
    }

    public static String modId() {
        return MOD_ID;
    }

    public static Logger logger() {
        return LOGGER;
    }

}
