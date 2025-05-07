package com.mikitellurium.turtlechargingstation.networking.payloads;

import com.mikitellurium.telluriumforge.networking.payload.BlockEntitySyncPayload;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationScreenHandler;
import com.mikitellurium.turtlechargingstation.util.FastId;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public class TurtleFuelSyncPayload extends BlockEntitySyncPayload<Integer> {
    public static final Id<TurtleFuelSyncPayload> ID = new Id<>(FastId.ofMod("turtle_fuel_sync"));
    public static final PacketCodec<PacketByteBuf, TurtleFuelSyncPayload> CODEC = getCodec(PacketCodecs.INTEGER, TurtleFuelSyncPayload::new);

    public TurtleFuelSyncPayload(BlockPos blockPos, int energy) {
        super(blockPos, energy);
    }

    @Override
    public void handle(ClientPlayNetworking.Context context) {
        if (context.player().currentScreenHandler instanceof TurtleChargingStationScreenHandler &&
                context.player().getWorld().getBlockEntity(this.getBlockPos()) instanceof TurtleBlockEntity turtle) {
            turtle.getAccess().setFuelLevel(this.getValue());
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
