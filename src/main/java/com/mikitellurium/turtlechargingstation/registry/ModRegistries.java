package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.Registrator;
import com.mikitellurium.turtlechargingstation.util.FastId;
import net.minecraft.registry.Registry;

public class ModRegistries {
    public static void register() {
        ModBlocks.REGISTRATOR.init();
        ModItems.REGISTRATOR.init();
        ModBlockEntities.REGISTRATOR.init();
        ModCreativeTab.REGISTRATOR.init();
        ModScreenHandlers.REGISTRATOR.init();
    }

    public static <T> Registrator<T> makeRegistrator(Registry<T> registry) {
        return new Registrator<>(registry, FastId.modId());
    }
}
