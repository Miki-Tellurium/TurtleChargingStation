package com.mikitellurium.turtlecharginstation.networking.packets;

import com.mikitellurium.telluriumforge.networking.ModPacket;
import com.mikitellurium.turtlecharginstation.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlecharginstation.gui.TurtleChargingStationMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SideTrackingSyncS2CPacket implements ModPacket {

    private final Direction side;
    private final BlockPos pos;

    public SideTrackingSyncS2CPacket(Direction side, BlockPos pos) {
        this.side = side;
        this.pos = pos;
    }

    public SideTrackingSyncS2CPacket(FriendlyByteBuf buf) {
        this.side = buf.readEnum(Direction.class);
        this.pos = buf.readBlockPos();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(side);
        buf.writeBlockPos(pos);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // Client
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof TurtleChargingStationBlockEntity blockEntity) {
                blockEntity.setReceivingEnergy(side);

                if(Minecraft.getInstance().player.containerMenu instanceof TurtleChargingStationMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {
                    blockEntity.setReceivingEnergy(side);
                }
            }
        });

        return true;
    }

}
