package com.mikitellurium.turtlechargingstation.networking;

import com.mikitellurium.telluriumforge.networking.NetworkingHelper;
import com.mikitellurium.turtlechargingstation.networking.packets.CableIdSyncS2CPacket;
import com.mikitellurium.turtlechargingstation.networking.packets.EnergySyncS2CPacket;
import com.mikitellurium.turtlechargingstation.networking.packets.TurtleFuelSyncS2CPacket;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.minecraftforge.network.NetworkDirection;

public class Networking {
    public static NetworkingHelper HELPER = NetworkingHelper.create(FastLoc.modId());

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        HELPER.registerPacket(id(), EnergySyncS2CPacket.class, EnergySyncS2CPacket::new, NetworkDirection.PLAY_TO_CLIENT);
        HELPER.registerPacket(id(), TurtleFuelSyncS2CPacket.class, TurtleFuelSyncS2CPacket::new, NetworkDirection.PLAY_TO_CLIENT);
        HELPER.registerPacket(id(), CableIdSyncS2CPacket.class, CableIdSyncS2CPacket::new, NetworkDirection.PLAY_TO_CLIENT);
    }

}
