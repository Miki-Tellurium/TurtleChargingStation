package com.mikitellurium.turtlechargingstation;

import com.mikitellurium.telluriumforge.event.EventHelper;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlechargingstation.common.integration.computercraft.TurtleChargingStationPeripheral;
import com.mikitellurium.turtlechargingstation.datagen.ModBlockTagsProvider;
import com.mikitellurium.turtlechargingstation.datagen.ModEntityTagsProvider;
import com.mikitellurium.turtlechargingstation.datagen.ModLootTableProvider;
import com.mikitellurium.turtlechargingstation.datagen.ModRecipeProvider;
import com.mikitellurium.turtlechargingstation.registry.ModCreativeTab;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.ForgeComputerCraftAPI;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.concurrent.CompletableFuture;

public class CommonSetup {

    public static void register(IEventBus modEventBus) {
        final EventHelper helper = new EventHelper();
        helper
                .addListener(modEventBus, ModCreativeTab::buildCreativeTab)
                .addListener(modEventBus, CommonSetup::commonSetup)
                .addListener(modEventBus, CommonSetup::gatherData)
                .registerAll();
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        ComputerCraftAPI.registerGenericSource(new TurtleChargingStationPeripheral());
        ForgeComputerCraftAPI.registerGenericCapability(TurtleChargingStationBlockEntity.ACCESS_CAP);
    }

    private static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        generator.addProvider(true, new ModRecipeProvider(generator));
        generator.addProvider(true, new ModLootTableProvider.Output(generator.getPackOutput()));
        generator.addProvider(true, new ModBlockTagsProvider(generator.getPackOutput(), lookupProvider, event.getExistingFileHelper()));
        generator.addProvider(true, new ModEntityTagsProvider(generator.getPackOutput(), lookupProvider, event.getExistingFileHelper()));
    }

}
