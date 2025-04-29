package com.mikitellurium.turtlechargingstation;

import com.mikitellurium.telluriumforge.event.EventHelper;
import com.mikitellurium.turtlechargingstation.common.blockentity.CopperCableBlockEntity;
import com.mikitellurium.turtlechargingstation.common.blockentity.ThunderchargeDynamoBlockEntity;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlechargingstation.common.integration.computercraft.TurtleChargingStationPeripheral;
import com.mikitellurium.turtlechargingstation.datagen.ModBlockTagsProvider;
import com.mikitellurium.turtlechargingstation.datagen.ModEntityTagsProvider;
import com.mikitellurium.turtlechargingstation.datagen.ModLootTableProvider;
import com.mikitellurium.turtlechargingstation.datagen.ModRecipeProvider;
import com.mikitellurium.turtlechargingstation.networking.payloads.CableIdSyncPayload;
import com.mikitellurium.turtlechargingstation.networking.payloads.EnergySyncPayload;
import com.mikitellurium.turtlechargingstation.networking.payloads.TurtleFuelSyncPayload;
import com.mikitellurium.turtlechargingstation.registry.ModCreativeTab;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.concurrent.CompletableFuture;

public class CommonSetup {

    public static void register(IEventBus modEventBus) {
        final EventHelper helper = new EventHelper();
        helper
                .addListener(modEventBus, ModCreativeTab::buildCreativeTab)
                .addListener(modEventBus, CommonSetup::gatherData)
                .addListener(modEventBus, CommonSetup::registerCapabilities)
                .addListener(modEventBus, CommonSetup::registerPayloads)
                .registerAll();
        ComputerCraftAPI.registerGenericSource(new TurtleChargingStationPeripheral());
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        TurtleChargingStationBlockEntity.registerCapabilities(event);
        ThunderchargeDynamoBlockEntity.registerCapabilities(event);
        CopperCableBlockEntity.registerCapabilities(event);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(FastLoc.modId());
        registrar.playToClient(EnergySyncPayload.TYPE, EnergySyncPayload.CODEC, EnergySyncPayload::handle);
        registrar.playToClient(TurtleFuelSyncPayload.TYPE, TurtleFuelSyncPayload.CODEC, TurtleFuelSyncPayload::handle);
        registrar.playToClient(CableIdSyncPayload.TYPE, CableIdSyncPayload.CODEC, CableIdSyncPayload::handle);
    }

    private static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        generator.addProvider(true, new ModRecipeProvider(generator, lookupProvider));
        generator.addProvider(true, new ModLootTableProvider.Output(generator.getPackOutput(), lookupProvider));
        generator.addProvider(true, new ModBlockTagsProvider(generator.getPackOutput(), lookupProvider, event.getExistingFileHelper()));
        generator.addProvider(true, new ModEntityTagsProvider(generator.getPackOutput(), lookupProvider, event.getExistingFileHelper()));
    }

}
