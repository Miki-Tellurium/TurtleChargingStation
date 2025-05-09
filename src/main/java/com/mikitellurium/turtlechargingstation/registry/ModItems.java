package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.Registrator;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static Registrator<Item> REGISTRATOR;

    static {
        REGISTRATOR = ModRegistries.makeRegistry(ForgeRegistries.ITEMS);
    }
}
