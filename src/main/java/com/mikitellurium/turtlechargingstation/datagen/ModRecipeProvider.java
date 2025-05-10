package com.mikitellurium.turtlechargingstation.datagen;

import com.mikitellurium.turtlechargingstation.registry.ModBlocks;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import com.mikitellurium.turtlechargingstation.util.ModIdConstants;
import dan200.computercraft.shared.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(DataGenerator generator, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(generator.getPackOutput(), lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ICondition vanillaRecipeCondition = not(or(
                modLoaded(ModIdConstants.ID_THERMAL),
                modLoaded(ModIdConstants.ID_MEKANISM),
                modLoaded(ModIdConstants.ID_POWAH)));
        // Vanilla
        this.turtleChargingStation(recipeOutput, Items.REDSTONE_BLOCK, "turtle_charging_station", vanillaRecipeCondition);
        this.thunderchargeDynamo(recipeOutput, Items.REDSTONE_BLOCK, Tags.Items.INGOTS_GOLD, "thundercharge_dynamo", vanillaRecipeCondition);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COPPER_CABLE.get(), 6)
                .pattern(" X ")
                .pattern("CCC")
                .pattern(" X ")
                .define('X', Items.HONEYCOMB)
                .define('C', Tags.Items.INGOTS_COPPER)
                .unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER))
                .unlockedBy("has_honeyComb", has(Items.HONEYCOMB))
                .save(recipeOutput, FastLoc.ofMod("copper_cable"));
        // Thermal
        if (ModList.get().isLoaded(ModIdConstants.ID_THERMAL)) {
            Item energyCellFrame = BuiltInRegistries.ITEM.get(FastLoc.of(ModIdConstants.ID_THERMAL, "energy_cell_frame"));
            Item machineFrame = BuiltInRegistries.ITEM.get(FastLoc.of(ModIdConstants.ID_THERMAL, "machine_frame"));
            Item rfCoil = BuiltInRegistries.ITEM.get(FastLoc.of(ModIdConstants.ID_THERMAL, "rf_coil"));
            this.turtleChargingStation(recipeOutput, energyCellFrame, "turtle_charging_station_thermal", modLoaded(ModIdConstants.ID_THERMAL));
            this.thunderchargeDynamo(recipeOutput, machineFrame, rfCoil, "thundercharge_dynamo_thermal", modLoaded(ModIdConstants.ID_THERMAL));
        }
        // Mekanism
        if (ModList.get().isLoaded(ModIdConstants.ID_MEKANISM)) {
            Item steelCasing = BuiltInRegistries.ITEM.get(FastLoc.of(ModIdConstants.ID_MEKANISM, "steel_casing"));
            Item osmiumIngot = BuiltInRegistries.ITEM.get(FastLoc.of(ModIdConstants.ID_MEKANISM, "ingot_osmium"));
            this.turtleChargingStation(recipeOutput, steelCasing, "turtle_charging_station_mekanism", modLoaded(ModIdConstants.ID_MEKANISM));
            this.thunderchargeDynamo(recipeOutput, steelCasing, osmiumIngot, "thundercharge_dynamo_mekanism", modLoaded(ModIdConstants.ID_MEKANISM));
        }
        // Powah
        if (ModList.get().isLoaded(ModIdConstants.ID_POWAH)) {
            Item dielectricCasing = BuiltInRegistries.ITEM.get(FastLoc.of(ModIdConstants.ID_POWAH, "dielectric_casing"));
            Item basicCapacitor = BuiltInRegistries.ITEM.get(FastLoc.of(ModIdConstants.ID_POWAH, "capacitor_basic"));
            this.turtleChargingStation(recipeOutput, dielectricCasing, "turtle_charging_station_powah", modLoaded(ModIdConstants.ID_POWAH));
            this.thunderchargeDynamo(recipeOutput, dielectricCasing, basicCapacitor, "thundercharge_dynamo_powah", modLoaded(ModIdConstants.ID_POWAH));
        }
    }

    private void turtleChargingStation(RecipeOutput recipeOutput, ItemLike coreItem, String recipeId, ICondition condition) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.TURTLE_CHARGING_STATION.get())
                .pattern("cgc")
                .pattern("gRg")
                .pattern("cIc")
                .define('c', Blocks.BLACK_CONCRETE)
                .define('g', Tags.Items.INGOTS_GOLD)
                .define('I', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('R', coreItem)
                .unlockedBy("has_turtle", has(ModRegistry.Blocks.TURTLE_NORMAL.get()))
                .unlockedBy("has_advanced_turtle", has(ModRegistry.Blocks.TURTLE_ADVANCED.get()))
                .save(recipeOutput.withConditions(condition), FastLoc.ofMod(recipeId));
    }

    private void thunderchargeDynamo(RecipeOutput recipeOutput, ItemLike coreItem, ItemLike secondItem, String recipeId, ICondition condition) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.THUNDERCHARGE_DYNAMO.get())
                .pattern("XRX")
                .pattern("X#X")
                .pattern("XGX")
                .define('X', Tags.Items.INGOTS_IRON)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('#', coreItem)
                .define('R', secondItem)
                .unlockedBy("has_turtle", has(ModRegistry.Blocks.TURTLE_NORMAL.get()))
                .unlockedBy("has_advanced_turtle", has(ModRegistry.Blocks.TURTLE_ADVANCED.get()))
                .save(recipeOutput.withConditions(condition), FastLoc.ofMod(recipeId));
    }

    private void thunderchargeDynamo(RecipeOutput recipeOutput, ItemLike coreItem, TagKey<Item> secondItem, String recipeId, ICondition condition) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.THUNDERCHARGE_DYNAMO.get())
                .pattern("XRX")
                .pattern("X#X")
                .pattern("XGX")
                .define('X', Tags.Items.INGOTS_IRON)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('#', coreItem)
                .define('R', secondItem)
                .unlockedBy("has_turtle", has(ModRegistry.Blocks.TURTLE_NORMAL.get()))
                .unlockedBy("has_advanced_turtle", has(ModRegistry.Blocks.TURTLE_ADVANCED.get()))
                .save(recipeOutput.withConditions(condition), FastLoc.ofMod(recipeId));
    }

}
