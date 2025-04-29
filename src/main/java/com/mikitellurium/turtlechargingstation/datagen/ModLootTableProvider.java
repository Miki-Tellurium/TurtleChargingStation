package com.mikitellurium.turtlechargingstation.datagen;

import com.mikitellurium.turtlechargingstation.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ModLootTableProvider extends BlockLootSubProvider {

    private ModLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.TURTLE_CHARGING_STATION.get());
        this.dropSelf(ModBlocks.THUNDERCHARGE_DYNAMO.get());
        this.dropSelf(ModBlocks.COPPER_CABLE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.REGISTRY.registry().getEntries().stream().map(DeferredHolder::get).collect(Collectors.toUnmodifiableSet());
    }

    public static class Output extends LootTableProvider {

        public Output(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, Set.of(), List.of(new LootTableProvider.SubProviderEntry(ModLootTableProvider::new, LootContextParamSets.BLOCK)), registries);
        }

    }

}
