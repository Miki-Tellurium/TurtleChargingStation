package com.mikitellurium.turtlechargingstation;

import com.mikitellurium.turtlechargingstation.client.blockentity.DebugCableRenderer;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationScreen;
import com.mikitellurium.turtlechargingstation.networking.ModMessages;
import com.mikitellurium.turtlechargingstation.registry.ModBlockEntities;
import com.mikitellurium.turtlechargingstation.registry.ModScreenHandlers;
import dan200.computercraft.client.render.TurtleBlockEntityRenderer;
import dan200.computercraft.shared.ModRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class TurtleChargingStationClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.TURTLE_CHARGING_STATION, TurtleChargingStationScreen::new);
        BlockEntityRendererFactories.register(ModBlockEntities.COPPER_CABLE, DebugCableRenderer::new);
        ModMessages.registerS2CPackets();
    }

}
