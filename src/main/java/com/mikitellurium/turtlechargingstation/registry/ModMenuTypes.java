package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.Registrator;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModMenuTypes {
    public static Registrator<MenuType<?>> REGISTRATOR;
    public static final DeferredHolder<MenuType<?>, MenuType<TurtleChargingStationMenu>> TURTLE_CHARGING_STATION;

    static {
        REGISTRATOR = ModRegistries.makeRegistry(Registries.MENU);
        TURTLE_CHARGING_STATION = REGISTRATOR.register("turtle_charging_station_menu", () -> IMenuTypeExtension.create(TurtleChargingStationMenu::new));
    }
}
