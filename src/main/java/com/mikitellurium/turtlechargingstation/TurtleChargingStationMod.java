package com.mikitellurium.turtlechargingstation;

import com.mikitellurium.turtlechargingstation.config.ModConfigs;
import com.mikitellurium.turtlechargingstation.registry.*;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

@Mod(TurtleChargingStationMod.MOD_ID)
public class TurtleChargingStationMod {

    public static final String MOD_ID = "turtlechargingstation";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TurtleChargingStationMod(IEventBus modEventBus) {
        this.registration(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreativeTab);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    public void addCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == ModCreativeTab.TAB_TURTLECHARGINGSTATION.get()) {
            event.accept(ModBlocks.TURTLE_CHARGING_STATION_BLOCK);
            event.accept(ModBlocks.THUNDERCHARGE_DYNAMO_BLOCK);
        }
    }

    private void registration(IEventBus modEventBus) {
        ModConfigs.register();
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTab.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModCapabilities.register(modEventBus);
    }

}
