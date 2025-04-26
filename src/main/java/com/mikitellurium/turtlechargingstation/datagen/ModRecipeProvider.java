package com.mikitellurium.turtlechargingstation.datagen;

import com.mikitellurium.turtlechargingstation.registry.ModBlocks;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import com.mikitellurium.turtlechargingstation.util.ModIdConstants;
import dan200.computercraft.shared.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(DataGenerator generator, CompletableFuture<HolderLookup.Provider> future) {
        super(generator.getPackOutput(), future);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        Item redstoneBlock = Items.REDSTONE_BLOCK;

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.TURTLE_CHARGING_STATION_BLOCK.get())
                .pattern("cgc")
                .pattern("gRg")
                .pattern("cIc")
                .define('c', Blocks.BLACK_CONCRETE)
                .define('g', Tags.Items.INGOTS_GOLD)
                .define('I', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('R', redstoneBlock)
                .unlockedBy("has_turtle", has(ModRegistry.Blocks.TURTLE_NORMAL.get()))
                .unlockedBy("has_advanced_turtle", has(ModRegistry.Blocks.TURTLE_ADVANCED.get()))
                .save(recipeOutput.withConditions(not(or(
                                modLoaded(ModIdConstants.ID_THERMAL),
                                modLoaded(ModIdConstants.ID_MEKANISM),
                                modLoaded(ModIdConstants.ID_POWAH)))),
                        FastLoc.modLoc("turtle_charging_station"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.THUNDERCHARGE_DYNAMO_BLOCK.get())
                .pattern("XRX")
                .pattern("X#X")
                .pattern("XGX")
                .define('X', Tags.Items.INGOTS_IRON)
                .define('R', Blocks.LIGHTNING_ROD)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('#', redstoneBlock)
                .unlockedBy("has_turtle", has(ModRegistry.Blocks.TURTLE_NORMAL.get()))
                .unlockedBy("has_advanced_turtle", has(ModRegistry.Blocks.TURTLE_ADVANCED.get()))
                .save(recipeOutput.withConditions(not(or(
                                modLoaded(ModIdConstants.ID_THERMAL),
                                modLoaded(ModIdConstants.ID_MEKANISM),
                                modLoaded(ModIdConstants.ID_POWAH)))),
                        FastLoc.modLoc("tundercharge_dynamo"));
        }

}
