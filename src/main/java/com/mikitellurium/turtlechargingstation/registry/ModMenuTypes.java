package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.RegistryHelper;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModMenuTypes {

    public static RegistryHelper<MenuType<?>> REGISTRY;
    public static final DeferredHolder<MenuType<?>, MenuType<TurtleChargingStationMenu>> TURTLE_CHARGING_STATION_GUI;

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String name, IContainerFactory<T> factory) {
        return REGISTRY.register(name, () -> IMenuTypeExtension.create(factory));
    }

    static {
        REGISTRY = ModRegistries.makeRegistry(Registries.MENU);
        TURTLE_CHARGING_STATION_GUI = register("turtle_charging_station_menu", TurtleChargingStationMenu::new);
    }

}
