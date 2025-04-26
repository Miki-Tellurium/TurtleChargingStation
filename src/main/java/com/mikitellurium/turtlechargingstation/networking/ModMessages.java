package com.mikitellurium.turtlechargingstation.networking;

import com.mikitellurium.turtlechargingstation.TurtleChargingStationMod;
import com.mikitellurium.turtlechargingstation.networking.payloads.EnergySyncPayload;
import com.mikitellurium.turtlechargingstation.networking.payloads.TurtleFuelSyncPayload;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = TurtleChargingStationMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModMessages {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(FastLoc.modId());
        registrar.playToClient(EnergySyncPayload.TYPE, EnergySyncPayload.CODEC, EnergySyncPayload::handle);
        registrar.playToClient(TurtleFuelSyncPayload.TYPE, TurtleFuelSyncPayload.CODEC, TurtleFuelSyncPayload::handle);
    }

}
