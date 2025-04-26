package com.mikitellurium.turtlechargingstation.networking.payloads;

import com.mikitellurium.turtlechargingstation.gui.TurtleChargingStationMenu;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class TurtleFuelSyncPayload extends IntSyncPayload {

    public static final CustomPacketPayload.Type<TurtleFuelSyncPayload> TYPE = BlockEntitySyncPayload.makeId(FastLoc.modLoc("turtle_fuel_sync"));
    public static final StreamCodec<FriendlyByteBuf, TurtleFuelSyncPayload> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            TurtleFuelSyncPayload::getBlockPos,
            ByteBufCodecs.INT,
            TurtleFuelSyncPayload::getValue,
            TurtleFuelSyncPayload::new);

    public TurtleFuelSyncPayload(BlockPos blockPos, Integer value) {
        super(blockPos, value);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(this.getBlockPos()) instanceof TurtleBlockEntity turtle) {
                turtle.getAccess().setFuelLevel(this.getValue());

                if(context.player().containerMenu instanceof TurtleChargingStationMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(this.getBlockPos())) {
                    turtle.getAccess().setFuelLevel(this.getValue());
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
