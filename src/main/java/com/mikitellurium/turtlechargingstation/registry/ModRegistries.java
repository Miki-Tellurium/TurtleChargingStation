package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.Registrator;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ModRegistries {
    public static void register() {
        ModBlocks.REGISTRATOR.init();
//        ModItems.REGISTRATOR.register();
//        ModBlockEntities.REGISTRATOR.register();
//        ModCreativeTab.REGISTRATOR.register();
//        ModMenuTypes.REGISTRATOR.register();
    }

    public static <T extends IForgeRegistryEntry<T>> Registrator<T> makeRegistry(IForgeRegistry<T> registry) {
        return Registrator.makeRegistrator(registry, FastLoc.modId());
    }
}
