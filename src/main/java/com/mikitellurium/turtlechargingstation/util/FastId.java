package com.mikitellurium.turtlechargingstation.util;

import com.mikitellurium.turtlechargingstation.TurtleChargingStationMod;
import net.minecraft.util.Identifier;

public class FastId {

    public static Identifier of(String namespace, String id) {
        return new Identifier(namespace, id);
    }

    public static Identifier ofMod(String id) {
        return of(modId(), id);
    }

    public static Identifier ofMc(String id) {
        return new Identifier(id);
    }

    public static String modId() {
        return TurtleChargingStationMod.MOD_ID;
    }

}
