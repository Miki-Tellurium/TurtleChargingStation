package com.mikitellurium.turtlechargingstation.datagen;

import com.mikitellurium.turtlechargingstation.registry.ModBlocks;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import com.mikitellurium.turtlechargingstation.util.ModIdConstants;
import dan200.computercraft.shared.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(DataGenerator generator) {
        super(generator.getPackOutput());
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        Item redstoneBlock = Items.REDSTONE_BLOCK;
        Item energyCellFrame = ForgeRegistries.ITEMS.getValue(FastLoc.of(ModIdConstants.ID_THERMAL, "energy_cell_frame"));
        Item machineFrame = ForgeRegistries.ITEMS.getValue(FastLoc.of(ModIdConstants.ID_THERMAL, "machine_frame"));
        Item rfCoil = ForgeRegistries.ITEMS.getValue(FastLoc.of(ModIdConstants.ID_THERMAL, "rf_coil"));
        Item steelCasing = ForgeRegistries.ITEMS.getValue(FastLoc.of(ModIdConstants.ID_MEKANISM, "steel_casing"));
        Item osmiumIngot = ForgeRegistries.ITEMS.getValue(FastLoc.of(ModIdConstants.ID_MEKANISM, "ingot_osmium"));
        Item dielectricCasing = ForgeRegistries.ITEMS.getValue(FastLoc.of(ModIdConstants.ID_POWAH, "dielectric_casing"));
        Item basicCapacitor = ForgeRegistries.ITEMS.getValue(FastLoc.of(ModIdConstants.ID_POWAH, "capacitor_basic"));

        this.turtleChargingStation(consumer, not(or(
                        modLoaded(ModIdConstants.ID_THERMAL),
                        modLoaded(ModIdConstants.ID_MEKANISM),
                        modLoaded(ModIdConstants.ID_POWAH))),
                redstoneBlock, "turtle_charging_station");
        ConditionalRecipe.builder()
                .addCondition(not(or(
                        modLoaded(ModIdConstants.ID_THERMAL),
                        modLoaded(ModIdConstants.ID_MEKANISM),
                        modLoaded(ModIdConstants.ID_POWAH))))
                .addRecipe(
                        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.THUNDERCHARGE_DYNAMO.get())
                                .pattern("XRX")
                                .pattern("X#X")
                                .pattern("XGX")
                                .define('X', Tags.Items.INGOTS_IRON)
                                .define('R', Blocks.LIGHTNING_ROD)
                                .define('G', Tags.Items.INGOTS_GOLD)
                                .define('#', redstoneBlock)
                                .unlockedBy("has_turtle", has(ModRegistry.Blocks.TURTLE_NORMAL.get()))
                                .unlockedBy("has_advanced_turtle", has(ModRegistry.Blocks.TURTLE_ADVANCED.get()))::save)
                .build(consumer,  FastLoc.ofMod("thundercharge_dynamo"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COPPER_CABLE.get(), 6)
                .pattern(" X ")
                .pattern("CCC")
                .pattern(" X ")
                .define('X', Items.HONEYCOMB)
                .define('C', Tags.Items.INGOTS_COPPER)
                .unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER))
                .unlockedBy("has_honeyComb", has(Items.HONEYCOMB))
                .save(consumer, FastLoc.ofMod("copper_cable"));

        if (ModList.get().isLoaded(ModIdConstants.ID_THERMAL)) {
            this.turtleChargingStation(consumer, and(
                            modLoaded(ModIdConstants.ID_THERMAL),
                            itemExists(ModIdConstants.ID_THERMAL, "energy_cell_frame")),
                    energyCellFrame, "turtle_charging_station_thermal");
            ConditionalRecipe.builder()
                    .addCondition(and(
                            modLoaded(ModIdConstants.ID_THERMAL),
                            itemExists(ModIdConstants.ID_THERMAL, "machine_frame"),
                            itemExists(ModIdConstants.ID_THERMAL, "rf_coil")))
                    .addRecipe(
                            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.THUNDERCHARGE_DYNAMO.get())
                                    .pattern("XRX")
                                    .pattern("X#X")
                                    .pattern("XGX")
                                    .define('X', Tags.Items.INGOTS_IRON)
                                    .define('G', Tags.Items.INGOTS_GOLD)
                                    .define('R', rfCoil)
                                    .define('#', machineFrame)
                                    .unlockedBy("has_turtle", has(ModRegistry.Blocks.TURTLE_NORMAL.get()))
                                    .unlockedBy("has_advanced_turtle", has(ModRegistry.Blocks.TURTLE_ADVANCED.get()))::save)
                    .build(consumer, FastLoc.ofMod("thundercharge_dynamo_thermal"));
        }

        if (ModList.get().isLoaded(ModIdConstants.ID_MEKANISM)) {
            this.turtleChargingStation(consumer, and(
                            modLoaded(ModIdConstants.ID_MEKANISM),
                            itemExists(ModIdConstants.ID_MEKANISM, "steel_casing")),
                    steelCasing, "turtle_charging_station_mekanism");
            ConditionalRecipe.builder()
                    .addCondition(and(
                            modLoaded(ModIdConstants.ID_MEKANISM),
                            itemExists(ModIdConstants.ID_MEKANISM, "steel_casing"),
                            itemExists(ModIdConstants.ID_MEKANISM, "ingot_osmium")))
                    .addRecipe(
                            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.THUNDERCHARGE_DYNAMO.get())
                                    .pattern("XRX")
                                    .pattern("X#X")
                                    .pattern("XGX")
                                    .define('X', Tags.Items.INGOTS_IRON)
                                    .define('G', Tags.Items.INGOTS_GOLD)
                                    .define('R', osmiumIngot)
                                    .define('#', steelCasing)
                                    .unlockedBy("has_turtle", has(ModRegistry.Blocks.TURTLE_NORMAL.get()))
                                    .unlockedBy("has_advanced_turtle", has(ModRegistry.Blocks.TURTLE_ADVANCED.get()))::save)
                    .build(consumer, FastLoc.ofMod("thundercharge_dynamo_mekanism"));
        }

        if (ModList.get().isLoaded(ModIdConstants.ID_POWAH)) {
            this.turtleChargingStation(consumer, and(
                            modLoaded(ModIdConstants.ID_POWAH),
                            itemExists(ModIdConstants.ID_POWAH, "dielectric_casing")),
                    dielectricCasing, "turtle_charging_station_powah");
            ConditionalRecipe.builder()
                    .addCondition(and(
                            modLoaded(ModIdConstants.ID_POWAH),
                            itemExists(ModIdConstants.ID_POWAH, "dielectric_casing"),
                            itemExists(ModIdConstants.ID_POWAH, "capacitor_basic")))
                    .addRecipe(
                            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.THUNDERCHARGE_DYNAMO.get())
                                    .pattern("XRX")
                                    .pattern("X#X")
                                    .pattern("XGX")
                                    .define('X', Tags.Items.INGOTS_IRON)
                                    .define('G', Tags.Items.INGOTS_GOLD)
                                    .define('R', basicCapacitor)
                                    .define('#', dielectricCasing)
                                    .unlockedBy("has_turtle", has(ModRegistry.Blocks.TURTLE_NORMAL.get()))
                                    .unlockedBy("has_advanced_turtle", has(ModRegistry.Blocks.TURTLE_ADVANCED.get()))::save)
                    .build(consumer, FastLoc.ofMod("thundercharge_dynamo_powah"));
        }
    }

    private void turtleChargingStation(Consumer<FinishedRecipe> consumer, ICondition condition, ItemLike item, String path) {
        ConditionalRecipe.builder()
                .addCondition(condition)
                .addRecipe(
                        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.TURTLE_CHARGING_STATION.get())
                                .pattern("cgc")
                                .pattern("gRg")
                                .pattern("cIc")
                                .define('c', Blocks.BLACK_CONCRETE)
                                .define('g', Tags.Items.INGOTS_GOLD)
                                .define('I', Tags.Items.STORAGE_BLOCKS_IRON)
                                .define('R', item)
                                .unlockedBy("has_turtle", has(ModRegistry.Blocks.TURTLE_NORMAL.get()))
                                .unlockedBy("has_advanced_turtle", has(ModRegistry.Blocks.TURTLE_ADVANCED.get()))::save)
                .build(consumer,  FastLoc.ofMod(path));
    }

}
