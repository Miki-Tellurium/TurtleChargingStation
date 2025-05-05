package com.mikitellurium.turtlechargingstation.datagen;

import com.mikitellurium.turtlechargingstation.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class ModLootTableProvider extends FabricBlockLootTableProvider {

    protected ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        this.addDrop(ModBlocks.TURTLE_CHARGING_STATION, this::nameableContainerDrops);
        this.addDrop(ModBlocks.THUNDERCHARGE_DYNAMO);
        this.addDrop(ModBlocks.COPPER_CABLE);
    }
}
