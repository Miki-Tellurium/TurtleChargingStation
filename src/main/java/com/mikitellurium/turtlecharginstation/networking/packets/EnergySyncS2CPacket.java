package com.mikitellurium.turtlecharginstation.networking.packets;

import com.mikitellurium.telluriumforge.networking.SimplePacket;
import com.mikitellurium.turtlecharginstation.client.gui.TurtleChargingStationMenu;
import com.mikitellurium.turtlecharginstation.common.blockentity.TurtleChargingStationBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EnergySyncS2CPacket implements SimplePacket {

    private final int energy;
    private final BlockPos pos;

    public EnergySyncS2CPacket(int energy, BlockPos pos) {
        this.energy = energy;
        this.pos = pos;
    }

    public EnergySyncS2CPacket(FriendlyByteBuf buf) {
        this.energy = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(energy);
        buf.writeBlockPos(pos);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // Client
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof TurtleChargingStationBlockEntity blockEntity) {
                blockEntity.setEnergy(energy);

                if(Minecraft.getInstance().player.containerMenu instanceof TurtleChargingStationMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {
                    blockEntity.setEnergy(energy);
                }
            }
        });

        return true;
    }

}
