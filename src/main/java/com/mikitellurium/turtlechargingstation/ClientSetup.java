package com.mikitellurium.turtlechargingstation;

import com.mikitellurium.turtlechargingstation.client.blockentity.DebugCableRenderer;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationScreen;
import com.mikitellurium.turtlechargingstation.registry.ModBlockEntities;
import com.mikitellurium.turtlechargingstation.registry.ModMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = TurtleChargingStationMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void clientSetup(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.TURTLE_CHARGING_STATION_GUI.get(), TurtleChargingStationScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
        if (!FMLLoader.isProduction()) {
            event.registerBlockEntityRenderer(ModBlockEntities.COPPER_CABLE.get(), DebugCableRenderer::new);
        }
    }

}
