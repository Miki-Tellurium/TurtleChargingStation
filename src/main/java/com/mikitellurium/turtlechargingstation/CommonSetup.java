package com.mikitellurium.turtlechargingstation;

import com.mikitellurium.telluriumforge.event.EventHelper;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationTileEntity;
import com.mikitellurium.turtlechargingstation.common.integration.computercraft.TurtleChargingStationPeripheral;
import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraft.tileentity.TileEntity;

public class CommonSetup {
    public static void register() {
        new EventHelper()
                .registerClass(CommonSetup.class)
                .registerAll();
        registerCCPeripherals();
    }

    private static void registerCCPeripherals() {
        ComputerCraftAPI.registerPeripheralProvider(((world, pos, direction) -> {
            TileEntity tile = world.getTileEntity(pos);
            return tile instanceof TurtleChargingStationTileEntity ? new TurtleChargingStationPeripheral((TurtleChargingStationTileEntity)tile) : null;
        }));
    }
}
