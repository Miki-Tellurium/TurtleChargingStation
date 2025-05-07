package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.Registrator;
import com.mikitellurium.turtlechargingstation.client.gui.TurtleChargingStationScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {
    public static final Registrator<ScreenHandlerType<?>> REGISTRATOR;
    public static final ScreenHandlerType<TurtleChargingStationScreenHandler> TURTLE_CHARGING_STATION;

    static {
        REGISTRATOR = ModRegistries.makeRegistrator(Registries.SCREEN_HANDLER);
        TURTLE_CHARGING_STATION = REGISTRATOR.register("turtle_charging_station", () -> new ExtendedScreenHandlerType<>(TurtleChargingStationScreenHandler::new, BlockPos.PACKET_CODEC));
    }
}
