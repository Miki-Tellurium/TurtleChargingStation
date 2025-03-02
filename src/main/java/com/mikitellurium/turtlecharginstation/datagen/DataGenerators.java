package com.mikitellurium.turtlecharginstation.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.concurrent.CompletableFuture;

public class DataGenerators {

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        generator.addProvider(event.includeServer(), new ModRecipeProvider(generator));
        generator.addProvider(event.includeServer(), new ModBlockTagsProvider(generator.getPackOutput(), lookupProvider, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ModEntityTagsProvider(generator.getPackOutput(), lookupProvider, event.getExistingFileHelper()));
    }

}
