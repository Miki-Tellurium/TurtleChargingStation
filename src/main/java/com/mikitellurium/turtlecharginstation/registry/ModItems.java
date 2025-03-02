package com.mikitellurium.turtlecharginstation.registry;

import com.mikitellurium.telluriumforge.registry.RegistryHelper;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static RegistryHelper<Item> REGISTRY;

    static {
        REGISTRY = ModRegistries.makeRegistry(ForgeRegistries.ITEMS);
    }

}
