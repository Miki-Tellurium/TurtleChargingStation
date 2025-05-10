package com.mikitellurium.turtlechargingstation;

import com.mikitellurium.turtlechargingstation.client.blockentity.DebugCableRenderer;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationScreen;
import com.mikitellurium.turtlechargingstation.networking.ModMessages;
import com.mikitellurium.turtlechargingstation.registry.ModBlockEntities;
import com.mikitellurium.turtlechargingstation.registry.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class TurtleChargingStationClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.TURTLE_CHARGING_STATION, TurtleChargingStationScreen::new);
        ModMessages.registerS2CPackets();
        if (FabricLoaderImpl.INSTANCE.isDevelopmentEnvironment()) {
            BlockEntityRendererFactories.register(ModBlockEntities.COPPER_CABLE, DebugCableRenderer::new);
        }
    }
}
