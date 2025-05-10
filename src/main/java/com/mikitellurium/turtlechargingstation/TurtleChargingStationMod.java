package com.mikitellurium.turtlechargingstation;

import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlechargingstation.common.integration.computercraft.TurtleChargingStationPeripheral;
import com.mikitellurium.turtlechargingstation.config.ModConfig;
import com.mikitellurium.turtlechargingstation.common.event.GameplayEvents;
import com.mikitellurium.turtlechargingstation.networking.ModMessages;
import com.mikitellurium.turtlechargingstation.registry.ModRegistries;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.impl.Peripherals;
import dan200.computercraft.shared.peripheral.generic.ComponentLookup;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jspecify.annotations.Nullable;
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
		ModMessages.registerC2SPackets();
		ComputerCraftAPI.registerGenericSource(new TurtleChargingStationPeripheral());
		Peripherals.addGenericLookup((world, pos, state, blockEntity, side, invalidate) -> TurtleChargingStationBlockEntity.ACCESS_LOOKUP.find(world, pos, state, blockEntity, side));
	}

	public static String modId() {
		return MOD_ID;
	}

	public static Logger logger() {
		return LOGGER;
	}
}