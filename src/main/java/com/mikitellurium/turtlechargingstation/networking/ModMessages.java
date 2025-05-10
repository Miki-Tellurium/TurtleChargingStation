package com.mikitellurium.turtlechargingstation.networking;

import com.mikitellurium.turtlechargingstation.networking.payloads.CableIdSyncPayload;
import com.mikitellurium.turtlechargingstation.networking.payloads.EnergySyncPayload;
import com.mikitellurium.turtlechargingstation.networking.payloads.TurtleFuelSyncPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ModMessages {
    public static void registerC2SPackets() {
        ClientPlayNetworking.registerGlobalReceiver(EnergySyncPayload.ID, EnergySyncPayload::handle);
        ClientPlayNetworking.registerGlobalReceiver(TurtleFuelSyncPayload.ID, TurtleFuelSyncPayload::handle);
        ClientPlayNetworking.registerGlobalReceiver(CableIdSyncPayload.ID, CableIdSyncPayload::handle);
    }

    public static void registerS2CPackets() {
        PayloadTypeRegistry.playS2C().register(EnergySyncPayload.ID, EnergySyncPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(TurtleFuelSyncPayload.ID, TurtleFuelSyncPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CableIdSyncPayload.ID, CableIdSyncPayload.CODEC);
    }
}
