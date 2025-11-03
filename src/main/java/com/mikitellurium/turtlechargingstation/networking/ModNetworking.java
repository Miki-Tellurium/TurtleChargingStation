package com.mikitellurium.turtlechargingstation.networking;

import com.mikitellurium.telluriumforge.TelluriumForge;
import com.mikitellurium.turtlechargingstation.networking.packet.EnergySyncS2CPacket;
import com.mikitellurium.turtlechargingstation.networking.packet.TurtleFuelSyncS2CPacket;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModNetworking {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(TelluriumForge.modId());
    private static int id = 0;
    private static int id() {
        return id++;
    }

    public static void register() {
        INSTANCE.registerMessage(EnergySyncS2CPacket.handler(), EnergySyncS2CPacket.class, id(), Side.CLIENT);
        INSTANCE.registerMessage(TurtleFuelSyncS2CPacket.handler(), TurtleFuelSyncS2CPacket.class, id(), Side.CLIENT);
    }
}
