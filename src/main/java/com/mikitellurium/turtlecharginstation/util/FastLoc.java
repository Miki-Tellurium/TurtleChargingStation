package com.mikitellurium.turtlecharginstation.util;

import com.mikitellurium.turtlecharginstation.TurtleChargingStationMod;
import net.minecraft.resources.ResourceLocation;

public class FastLoc {
    public static ResourceLocation of(String namespace, String id) {
        return new ResourceLocation(namespace, id);
    }

    public static ResourceLocation ofMod(String id) {
        return of(modId(), id);
    }

    public static ResourceLocation ofMc(String id) {
        return new ResourceLocation(id);
    }

    public static String modId() {
        return TurtleChargingStationMod.MOD_ID;
    }
}
