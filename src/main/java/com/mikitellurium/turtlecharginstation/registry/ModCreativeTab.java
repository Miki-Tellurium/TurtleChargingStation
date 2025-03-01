package com.mikitellurium.turtlecharginstation.registry;

import com.mikitellurium.turtlecharginstation.TurtleChargingStationMod;
import com.mikitellurium.turtlecharginstation.util.FastLoc;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FastLoc.modId());

    public static final RegistryObject<CreativeModeTab> TAB_TURTLECHARGINGSTATION = CREATIVE_TABS.register(
            "creative_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativemodetab.turtlechargingstation_creative_tab"))
            .icon(() -> new ItemStack(ModBlocks.TURTLE_CHARGING_STATION_BLOCK.get()))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }

}
