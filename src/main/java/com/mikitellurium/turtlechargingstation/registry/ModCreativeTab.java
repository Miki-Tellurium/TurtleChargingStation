package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.Registrator;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

public class ModCreativeTab {

    public static final Registrator<ItemGroup> REGISTRATOR;
    public static final ItemGroup TAB_TURTLECHARGINGSTATION;

    static {
        REGISTRATOR = ModRegistries.makeRegistrator(Registries.ITEM_GROUP);
        TAB_TURTLECHARGINGSTATION = REGISTRATOR.register("creative_tab",
                () -> FabricItemGroup.builder()
                        .icon(() -> new ItemStack(ModBlocks.TURTLE_CHARGING_STATION.asItem()))
                        .displayName(Text.translatable("creativemodetab.turtlechargingstation_creative_tab"))
                        .entries((context, entries) -> {
                            entries.add(ModBlocks.TURTLE_CHARGING_STATION);
                            entries.add(ModBlocks.THUNDERCHARGE_DYNAMO);
                            entries.add(ModBlocks.COPPER_CABLE);
                        }).build());
    }

}
