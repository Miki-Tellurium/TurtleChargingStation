package com.mikitellurium.turtlechargingstation;

import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlechargingstation.common.integration.computercraft.TurtleChargingStationPeripheral;
import com.mikitellurium.turtlechargingstation.config.ModConfig;
import com.mikitellurium.turtlechargingstation.common.event.GameplayEvents;
import com.mikitellurium.turtlechargingstation.networking.ModMessages;
import com.mikitellurium.turtlechargingstation.registry.ModRegistries;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.impl.Peripherals;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TurtleChargingStationMod implements ModInitializer {
	private static final String MOD_ID = "turtlechargingstation";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModRegistries.register();
		GameplayEvents.register();
		ModConfig.register();
		ModMessages.registerS2CPackets();
		ComputerCraftAPI.registerGenericSource(new TurtleChargingStationPeripheral());
		Peripherals.addGenericLookup(TurtleChargingStationBlockEntity.ACCESS_LOOKUP::find);
	}

	public static String modId() {
		return MOD_ID;
	}

	public static Logger logger() {
		return LOGGER;
	}
}