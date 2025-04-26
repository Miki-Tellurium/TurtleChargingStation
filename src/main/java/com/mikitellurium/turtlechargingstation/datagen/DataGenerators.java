package com.mikitellurium.turtlechargingstation.datagen;

import com.mikitellurium.turtlechargingstation.TurtleChargingStationMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = TurtleChargingStationMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        generator.addProvider(event.includeServer(), new ModRecipeProvider(generator, lookupProvider));
        generator.addProvider(event.includeServer(), new ModTagsProvider(generator.getPackOutput(), lookupProvider, event.getExistingFileHelper()));
    }

}
