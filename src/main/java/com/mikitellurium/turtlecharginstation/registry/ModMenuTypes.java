package com.mikitellurium.turtlecharginstation.registry;

import com.mikitellurium.telluriumforge.registry.RegistryHelper;
import com.mikitellurium.turtlecharginstation.client.gui.TurtleChargingStationMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {

    public static RegistryHelper<MenuType<?>> REGISTRY;
    public static final RegistryObject<MenuType<TurtleChargingStationMenu>> TURTLE_CHARGING_STATION_GUI;

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String name, IContainerFactory<T> factory) {
        return REGISTRY.register(name, () -> IForgeMenuType.create(factory));
    }

    static {
        REGISTRY = ModRegistries.makeRegistry(ForgeRegistries.MENU_TYPES);
        TURTLE_CHARGING_STATION_GUI = register("turtle_charging_station_menu", TurtleChargingStationMenu::new);
    }

}
