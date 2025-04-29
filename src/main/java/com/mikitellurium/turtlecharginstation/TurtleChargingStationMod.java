package com.mikitellurium.turtlecharginstation;

import com.mikitellurium.turtlecharginstation.client.blockentity.DebugCableRenderer;
import com.mikitellurium.turtlecharginstation.common.integration.computercraft.TurtleChargingStationPeripheral;
import com.mikitellurium.turtlecharginstation.config.ModConfigs;
import com.mikitellurium.turtlecharginstation.common.event.ModEvents;
import com.mikitellurium.turtlecharginstation.client.gui.TurtleChargingStationScreen;
import com.mikitellurium.turtlecharginstation.networking.Networking;
import com.mikitellurium.turtlecharginstation.registry.ModBlockEntities;
import com.mikitellurium.turtlecharginstation.registry.ModMenuTypes;
import com.mikitellurium.turtlecharginstation.registry.ModRegistries;
import com.mojang.logging.LogUtils;
import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.slf4j.Logger;

@Mod(TurtleChargingStationMod.MOD_ID)
public class TurtleChargingStationMod {

    public static final String MOD_ID = "turtlechargingstation";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TurtleChargingStationMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        ModRegistries.register();
        ModEvents.register(modEventBus);
        Networking.register();
        ModConfigs.registerConfig();
        ComputerCraftAPI.registerGenericSource(new TurtleChargingStationPeripheral());
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.TURTLE_CHARGING_STATION_GUI.get(), TurtleChargingStationScreen::new);
        }

        @SubscribeEvent
        public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
            if (!FMLLoader.isProduction()) {
                event.registerBlockEntityRenderer(ModBlockEntities.COPPER_CABLE.get(), DebugCableRenderer::new);
            }
        }
    }

}
