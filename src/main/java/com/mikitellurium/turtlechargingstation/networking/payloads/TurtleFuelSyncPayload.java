package com.mikitellurium.turtlechargingstation.networking.payloads;

import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationMenu;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class TurtleFuelSyncPayload extends IntSyncPayload {

    public static final CustomPacketPayload.Type<TurtleFuelSyncPayload> TYPE = new Type<>(FastLoc.ofMod("turtle_fuel_sync"));
    public static final StreamCodec<FriendlyByteBuf, TurtleFuelSyncPayload> CODEC = IntSyncPayload.getCodec(TurtleFuelSyncPayload::new);

    public TurtleFuelSyncPayload(BlockPos blockPos, Integer value) {
        super(blockPos, value);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().containerMenu instanceof TurtleChargingStationMenu && Minecraft.getInstance().level.getBlockEntity(this.getBlockPos()) instanceof TurtleBlockEntity turtle) {
                turtle.getAccess().setFuelLevel(this.getValue());
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
