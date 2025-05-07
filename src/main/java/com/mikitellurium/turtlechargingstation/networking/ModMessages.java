package com.mikitellurium.turtlechargingstation.networking;

import com.mikitellurium.telluriumforge.networking.NetworkingHelper;
import com.mikitellurium.turtlechargingstation.networking.payloads.CableIdSyncPayload;
import com.mikitellurium.turtlechargingstation.networking.payloads.EnergySyncPayload;
import com.mikitellurium.turtlechargingstation.networking.payloads.TurtleFuelSyncPayload;

public class ModMessages {

    public static void registerC2SPackets() {
    }

    public static void registerS2CPackets() {
        NetworkingHelper.registerS2C(EnergySyncPayload.ID, EnergySyncPayload.CODEC, EnergySyncPayload::handle);
        NetworkingHelper.registerS2C(TurtleFuelSyncPayload.ID, TurtleFuelSyncPayload.CODEC, TurtleFuelSyncPayload::handle);
        NetworkingHelper.registerS2C(CableIdSyncPayload.ID, CableIdSyncPayload.CODEC, CableIdSyncPayload::handle);
    }

}
