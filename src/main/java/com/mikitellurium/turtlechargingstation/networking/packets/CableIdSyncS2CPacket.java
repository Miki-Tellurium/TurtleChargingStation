package com.mikitellurium.turtlechargingstation.networking.packets;

import com.mikitellurium.telluriumforge.networking.SimplePacket;
import com.mikitellurium.turtlechargingstation.common.blockentity.CopperCableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CableIdSyncS2CPacket implements SimplePacket {

    private final int id;
    private final BlockPos pos;

    public CableIdSyncS2CPacket(int id, BlockPos pos) {
        this.id = id;
        this.pos = pos;
    }

    public CableIdSyncS2CPacket(FriendlyByteBuf buf) {
        this.id = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(id);
        buf.writeBlockPos(pos);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // Client
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof CopperCableBlockEntity blockEntity) {
                blockEntity.setClientNetworkId(id);
            }
        });

        return true;
    }

}
