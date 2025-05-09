package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.Registrator;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.IForgeRegistry;

public class ModRegistries {
    public static void register() {
        ModBlocks.REGISTRATOR.register();
        ModItems.REGISTRATOR.register();
        ModBlockEntities.REGISTRATOR.register();
        ModCreativeTab.REGISTRATOR.register();
        ModMenuTypes.REGISTRATOR.register();
    }

    public static <T> Registrator<T> makeRegistry(ResourceKey<Registry<T>> resourceKey) {
        return Registrator.makeRegistrator(resourceKey, FastLoc.modId());
    }

    public static <T> Registrator<T> makeRegistry(IForgeRegistry<T> registry) {
        return makeRegistry(registry.getRegistryKey());
    }
}
