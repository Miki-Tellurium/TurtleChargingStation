package com.mikitellurium.turtlechargingstation.datagen;

import com.mikitellurium.turtlechargingstation.registry.ModBlocks;
import dan200.computercraft.shared.ModRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput generator, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(generator, registryLookup);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.TURTLE_CHARGING_STATION)
                .pattern("cgc")
                .pattern("gRg")
                .pattern("cIc")
                .input('c', Blocks.BLACK_CONCRETE)
                .input('g', Items.GOLD_INGOT)
                .input('R', Items.REDSTONE_BLOCK)
                .input('I', Items.IRON_BLOCK)
                .criterion(FabricRecipeProvider.hasItem(ModRegistry.Items.TURTLE_NORMAL.get()), FabricRecipeProvider.conditionsFromItem(ModRegistry.Items.TURTLE_NORMAL.get()))
                .criterion(FabricRecipeProvider.hasItem(ModRegistry.Items.TURTLE_ADVANCED.get()), FabricRecipeProvider.conditionsFromItem(ModRegistry.Items.TURTLE_ADVANCED.get()))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.THUNDERCHARGE_DYNAMO)
                .pattern("XRX")
                .pattern("X#X")
                .pattern("XGX")
                .input('G', Items.GOLD_INGOT)
                .input('R', Blocks.LIGHTNING_ROD)
                .input('#', Items.REDSTONE_BLOCK)
                .input('X', Items.IRON_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModRegistry.Items.TURTLE_NORMAL.get()), FabricRecipeProvider.conditionsFromItem(ModRegistry.Items.TURTLE_NORMAL.get()))
                .criterion(FabricRecipeProvider.hasItem(ModRegistry.Items.TURTLE_ADVANCED.get()), FabricRecipeProvider.conditionsFromItem(ModRegistry.Items.TURTLE_ADVANCED.get()))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.COPPER_CABLE, 6)
                .pattern(" X ")
                .pattern("CCC")
                .pattern(" X ")
                .input('C', Items.COPPER_INGOT)
                .input('X', Items.HONEYCOMB)
                .criterion(FabricRecipeProvider.hasItem(Items.COPPER_INGOT), FabricRecipeProvider.conditionsFromItem(Items.COPPER_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.HONEYCOMB), FabricRecipeProvider.conditionsFromItem(Items.HONEYCOMB))
                .offerTo(exporter);
    }

}
