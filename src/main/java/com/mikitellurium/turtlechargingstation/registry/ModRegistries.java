package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.Registrator;
import com.mikitellurium.telluriumforge.registry.RegistryHelper;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ModRegistries {

    public static void register() {
        ModBlocks.REGISTRY.register();
        ModItems.REGISTRY.register();
        ModBlockEntities.REGISTRY.register();
        ModCreativeTab.REGISTRY.register();
        ModMenuTypes.REGISTRY.register();
    }

    public static <T> RegistryHelper<T> makeRegistry(ResourceKey<Registry<T>> resourceKey) {
        return Registrator.makeRegistrator(resourceKey, FastLoc.modId());
    }

}
