package com.mikitellurium.turtlechargingstation.networking.payloads;

import com.mikitellurium.turtlechargingstation.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlechargingstation.gui.TurtleChargingStationMenu;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class EnergySyncPayload extends IntSyncPayload {

    public static final CustomPacketPayload.Type<EnergySyncPayload> TYPE = BlockEntitySyncPayload.makeId(FastLoc.modLoc("energy_sync"));
    public static final StreamCodec<FriendlyByteBuf, EnergySyncPayload> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            EnergySyncPayload::getBlockPos,
            ByteBufCodecs.INT,
            EnergySyncPayload::getValue,
            EnergySyncPayload::new);

    public EnergySyncPayload(BlockPos blockPos, Integer value) {
        super(blockPos, value);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(this.getBlockPos()) instanceof TurtleChargingStationBlockEntity station) {
                station.setClientEnergy(this.getValue());

                if(context.player().containerMenu instanceof TurtleChargingStationMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(this.getBlockPos())) {
                    station.setClientEnergy(this.getValue());
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
