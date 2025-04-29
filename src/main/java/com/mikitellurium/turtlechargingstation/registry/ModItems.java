package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.ItemRegistrator;
import com.mikitellurium.telluriumforge.registry.RegistryHelper;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class ModItems {

    public static ItemRegistrator REGISTRY;

    static {
        REGISTRY = ItemRegistrator.makeRegistrator(FastLoc.modId());
    }

}
