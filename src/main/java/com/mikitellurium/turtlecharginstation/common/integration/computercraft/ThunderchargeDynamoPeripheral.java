package com.mikitellurium.turtlecharginstation.common.integration.computercraft;

import com.mikitellurium.turtlecharginstation.common.block.ThunderchargeDynamoBlock;
import com.mikitellurium.turtlecharginstation.common.blockentity.ThunderchargeDynamoBlockEntity;
import com.mikitellurium.turtlecharginstation.common.capability.PeripheralProvider;
import com.mikitellurium.turtlecharginstation.util.FastLoc;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ThunderchargeDynamoPeripheral implements IPeripheral {

    private static final ResourceLocation ID = FastLoc.modLoc("thundercharge_dynamo");
    private final ThunderchargeDynamoBlockEntity dynamo;

    public ThunderchargeDynamoPeripheral(ThunderchargeDynamoBlockEntity dynamo) {
        this.dynamo = dynamo;
    }

    @LuaFunction
    public final int getCharge() {
        return dynamo.getCharge();
    }

    @LuaFunction
    public final boolean isPowered() {
        return dynamo.getBlockState().getValue(ThunderchargeDynamoBlock.POWERED);
    }

    @Override
    public String getType() {
        return "thundercharge_dynamo";
    }

    @Override
    public boolean equals(IPeripheral other) {
        return other instanceof ThunderchargeDynamoPeripheral && this.dynamo == ((ThunderchargeDynamoPeripheral) other).dynamo;
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof ThunderchargeDynamoBlockEntity dynamo) {
            event.addCapability(ID, new PeripheralProvider<>(dynamo, ThunderchargeDynamoPeripheral::new));
        }
    }

}
