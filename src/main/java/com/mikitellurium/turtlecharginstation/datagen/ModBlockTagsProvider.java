package com.mikitellurium.turtlecharginstation.datagen;

import com.mikitellurium.turtlecharginstation.registry.ModBlocks;
import com.mikitellurium.turtlecharginstation.registry.ModTags;
import com.mikitellurium.turtlecharginstation.util.FastLoc;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, FastLoc.modId(), existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.TURTLE_CHARGING_STATION.get())
                .add(ModBlocks.THUNDERCHARGE_DYNAMO.get());

        this.tag(ModTags.DYNAMO_CONDUCTIVE_BLOCKS)
                .add(Blocks.CHAIN)
                .add(Blocks.GOLD_BLOCK)
                .add(Blocks.COPPER_BLOCK)
                .add(Blocks.CUT_COPPER)
                .add(Blocks.WAXED_COPPER_BLOCK)
                .add(Blocks.WAXED_CUT_COPPER)
                .add(Blocks.LIGHTNING_ROD);
    }



}
