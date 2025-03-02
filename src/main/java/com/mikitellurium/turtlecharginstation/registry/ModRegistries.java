package com.mikitellurium.turtlecharginstation.registry;

import com.mikitellurium.telluriumforge.registry.Registrator;
import com.mikitellurium.telluriumforge.registry.RegistryHelper;
import com.mikitellurium.turtlecharginstation.util.FastLoc;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.IForgeRegistry;

public class ModRegistries {

    public static void register(IEventBus modEventBus) {
        ModBlocks.REGISTRY.register();
        ModItems.REGISTRY.register();
        ModBlockEntities.REGISTRY.register();
        ModCreativeTab.REGISTRY.register();
        ModMenuTypes.REGISTRY.register();
    }

    public static <T> RegistryHelper<T> makeRegistry(ResourceKey<Registry<T>> resourceKey) {
        return Registrator.makeRegistrator(resourceKey, FastLoc.modId());
    }

    public static <T> RegistryHelper<T> makeRegistry(IForgeRegistry<T> registry) {
        return makeRegistry(registry.getRegistryKey());
    }

}
