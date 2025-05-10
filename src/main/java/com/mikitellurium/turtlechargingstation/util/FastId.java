package com.mikitellurium.turtlechargingstation.util;

import com.mikitellurium.turtlechargingstation.TurtleChargingStationMod;
import net.minecraft.util.Identifier;

public class FastId {
    public static Identifier of(String namespace, String id) {
        return Identifier.of(namespace, id);
    }

    public static Identifier ofMod(String id) {
        return of(modId(), id);
    }

    public static Identifier ofMc(String id) {
        return Identifier.of(id);
    }

    public static String modId() {
        return TurtleChargingStationMod.modId();
    }
}
