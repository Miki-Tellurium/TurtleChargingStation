package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.Registrator;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

public class ModItems {
    public static final Registrator<Item> REGISTRATOR;

    static {
        REGISTRATOR = ModRegistries.makeRegistrator(Registries.ITEM);
    }
}
