package com.mikitellurium.turtlecharginstation.networking;

import com.mikitellurium.turtlecharginstation.networking.packets.EnergySyncS2CPacket;
import com.mikitellurium.turtlecharginstation.networking.packets.SideTrackingSyncS2CPacket;
import com.mikitellurium.turtlecharginstation.networking.packets.TurtleFuelSyncS2CPacket;
import com.mikitellurium.turtlecharginstation.util.FastLoc;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(FastLoc.modLoc("messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
        INSTANCE = net;

        net.messageBuilder(EnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EnergySyncS2CPacket::new)
                .encoder(EnergySyncS2CPacket::write)
                .consumerMainThread(EnergySyncS2CPacket::handle)
                .add();
        net.messageBuilder(TurtleFuelSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(TurtleFuelSyncS2CPacket::new)
                .encoder(TurtleFuelSyncS2CPacket::write)
                .consumerMainThread(TurtleFuelSyncS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

}
