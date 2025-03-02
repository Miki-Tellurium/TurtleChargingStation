package com.mikitellurium.turtlecharginstation.registry;

import com.mikitellurium.telluriumforge.registry.RegistryHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTab {

    public static RegistryHelper<CreativeModeTab> REGISTRY;
    public static final RegistryObject<CreativeModeTab> TAB_TURTLECHARGINGSTATION;

    static {
        REGISTRY = ModRegistries.makeRegistry(Registries.CREATIVE_MODE_TAB);
        TAB_TURTLECHARGINGSTATION = REGISTRY.register("creative_tab", () -> CreativeModeTab.builder()
                .title(Component.translatable("creativemodetab.turtlechargingstation_creative_tab"))
                .icon(() -> new ItemStack(ModBlocks.TURTLE_CHARGING_STATION_BLOCK.get()))
                .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                .build());
    }

    public static void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == TAB_TURTLECHARGINGSTATION.get()) {
            event.accept(ModBlocks.TURTLE_CHARGING_STATION_BLOCK);
            event.accept(ModBlocks.THUNDERCHARGE_DYNAMO_BLOCK);
        }
    }

}
