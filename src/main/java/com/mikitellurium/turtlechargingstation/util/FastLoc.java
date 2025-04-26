package com.mikitellurium.turtlechargingstation.util;

import com.mikitellurium.turtlechargingstation.TurtleChargingStationMod;
import net.minecraft.resources.ResourceLocation;

public class FastLoc {

    public static ResourceLocation of(String namespace, String id) {
        return ResourceLocation.fromNamespaceAndPath(namespace, id);
    }

    public static ResourceLocation modLoc(String id) {
        return of(modId(), id);
    }

    public static ResourceLocation mcLoc(String id) {
        return ResourceLocation.withDefaultNamespace(id);
    }

    public static String modId() {
        return TurtleChargingStationMod.MOD_ID;
    }

}
