package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.ItemRegistrator;
import com.mikitellurium.turtlechargingstation.util.FastLoc;

public class ModItems {
    public static ItemRegistrator REGISTRATOR;

    static {
        REGISTRATOR = ItemRegistrator.makeRegistrator(FastLoc.modId());
    }
}
