package com.mikitellurium.turtlechargingstation;

import com.mikitellurium.telluriumforge.event.EventHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommonSetup {
    public static void register() {
        new EventHelper()
                .registerClass(CommonSetup.class)
                .registerAll();
    }

    @SubscribeEvent
    public static void attachTileCapabilities(AttachCapabilitiesEvent<TileEntity> event) {
//        if (event.getObject() instanceof TurtleChargingStationTileEntity) {
//            event.addCapability(FastLoc.ofMod("turtle_charging_station.inventory"), event.getObject());
//        }
    }
}
