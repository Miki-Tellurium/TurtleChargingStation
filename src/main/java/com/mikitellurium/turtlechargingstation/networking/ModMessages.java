package com.mikitellurium.turtlechargingstation.networking;

import com.mikitellurium.turtlechargingstation.networking.packets.CableIdSyncPacket;
import com.mikitellurium.turtlechargingstation.networking.packets.EnergySyncPacket;
import com.mikitellurium.turtlechargingstation.networking.packets.TurtleFuelSyncPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ModMessages {

    public static void registerC2SPackets() {
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(EnergySyncPacket.TYPE, EnergySyncPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(TurtleFuelSyncPacket.TYPE, TurtleFuelSyncPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(CableIdSyncPacket.TYPE, CableIdSyncPacket::handle);

    }

}
