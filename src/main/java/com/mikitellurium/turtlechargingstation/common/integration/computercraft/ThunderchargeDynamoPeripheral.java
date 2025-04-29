package com.mikitellurium.turtlechargingstation.common.integration.computercraft;

import com.mikitellurium.turtlechargingstation.common.block.ThunderchargeDynamoBlock;
import com.mikitellurium.turtlechargingstation.common.blockentity.ThunderchargeDynamoBlockEntity;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.resources.ResourceLocation;

public class ThunderchargeDynamoPeripheral implements IPeripheral {

    private final ThunderchargeDynamoBlockEntity dynamo;

    public ThunderchargeDynamoPeripheral(ThunderchargeDynamoBlockEntity dynamo) {
        this.dynamo = dynamo;
    }

    @LuaFunction(mainThread = true)
    public final int getCharge() {
        return dynamo.getCharge();
    }

    @LuaFunction(mainThread = true)
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

}
