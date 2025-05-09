package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.Registrator;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static Registrator<MenuType<?>> REGISTRATOR;
    public static final RegistryObject<MenuType<TurtleChargingStationMenu>> TURTLE_CHARGING_STATION;

    static {
        REGISTRATOR = ModRegistries.makeRegistry(ForgeRegistries.MENU_TYPES);
        TURTLE_CHARGING_STATION = REGISTRATOR.register("turtle_charging_station_menu", () -> IForgeMenuType.create(TurtleChargingStationMenu::new));
    }
}
