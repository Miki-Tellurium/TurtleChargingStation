package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.turtlechargingstation.blockentity.ThunderchargeDynamoBlockEntity;
import com.mikitellurium.turtlechargingstation.blockentity.TurtleChargingStationBlockEntity;
import net.neoforged.bus.api.IEventBus;

public class ModCapabilities {

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(TurtleChargingStationBlockEntity::registerCapability);
        modEventBus.addListener(ThunderchargeDynamoBlockEntity::registerCapability);
    }

}
