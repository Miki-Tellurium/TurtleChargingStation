package com.mikitellurium.turtlechargingstation;

import com.mikitellurium.turtlechargingstation.client.blockentity.DebugCableRenderer;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationScreen;
import com.mikitellurium.turtlechargingstation.registry.ModBlockEntities;
import com.mikitellurium.turtlechargingstation.registry.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod.EventBusSubscriber(modid = TurtleChargingStationMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.TURTLE_CHARGING_STATION.get(), TurtleChargingStationScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
        if (!FMLLoader.isProduction()) {
            event.registerBlockEntityRenderer(ModBlockEntities.COPPER_CABLE.get(), DebugCableRenderer::new);
        }
    }
}
