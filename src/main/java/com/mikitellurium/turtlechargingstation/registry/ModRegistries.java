package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.Registrator;
import com.mikitellurium.turtlechargingstation.common.blockentity.EnergyTestTile;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationTileEntity;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ModRegistries {
    public static void register() {
        ModBlocks.REGISTRATOR.init();
        registerTileEntities();
    }

    private static void registerTileEntities() {
        GameRegistry.registerTileEntity(TurtleChargingStationTileEntity.class, FastLoc.ofMod("turtle_charging_station"));
    }

    public static <T extends IForgeRegistryEntry<T>> Registrator<T> makeRegistry(IForgeRegistry<T> registry) {
        return Registrator.makeRegistrator(registry, FastLoc.modId());
    }
}
