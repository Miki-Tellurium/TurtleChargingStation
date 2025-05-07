package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.Registrator;
import com.mikitellurium.turtlechargingstation.test.EnergyTestItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

public class ModItems {
    public static final Registrator<Item> REGISTRATOR;
    public static final Item ENERGY_TEST_ITEM;

    static {
        REGISTRATOR = ModRegistries.makeRegistrator(Registries.ITEM);
        ENERGY_TEST_ITEM = REGISTRATOR.register("energy_test_item", () -> new EnergyTestItem(new FabricItemSettings()));
    }
}
