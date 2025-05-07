package com.mikitellurium.turtlechargingstation.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack packOutput = fabricDataGenerator.createPack();
        packOutput.addProvider(ModRecipeProvider::new);
        packOutput.addProvider(ModBlockTagProvider::new);
        packOutput.addProvider(ModEntityTagProvider::new);
        packOutput.addProvider(ModLootTableProvider::new);
    }
}
