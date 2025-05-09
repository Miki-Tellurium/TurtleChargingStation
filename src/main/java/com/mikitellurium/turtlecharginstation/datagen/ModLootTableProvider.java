package com.mikitellurium.turtlecharginstation.datagen;

import com.mikitellurium.turtlecharginstation.registry.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Set;

public class ModLootTableProvider extends BlockLootSubProvider {
    private ModLootTableProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.TURTLE_CHARGING_STATION.get());
        this.dropSelf(ModBlocks.THUNDERCHARGE_DYNAMO.get());
        this.dropSelf(ModBlocks.COPPER_CABLE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.REGISTRATOR.registry().getEntries().stream().map(RegistryObject::get)::iterator;
    }

    public static class Output extends LootTableProvider {
        public Output(PackOutput packOutput) {
            super(packOutput, Set.of(), List.of(new LootTableProvider.SubProviderEntry(ModLootTableProvider::new, LootContextParamSets.BLOCK)));
        }
    }
}
