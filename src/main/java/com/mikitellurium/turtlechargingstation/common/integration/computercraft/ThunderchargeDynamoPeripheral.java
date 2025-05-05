package com.mikitellurium.turtlechargingstation.common.integration.computercraft;

import com.mikitellurium.turtlechargingstation.common.block.ThunderchargeDynamoBlock;
import com.mikitellurium.turtlechargingstation.common.blockentity.ThunderchargeDynamoBlockEntity;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;

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
        return dynamo.getCachedState().get(ThunderchargeDynamoBlock.POWERED);
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
