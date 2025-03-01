package com.mikitellurium.turtlecharginstation.registry;

import com.mikitellurium.turtlecharginstation.TurtleChargingStationMod;
import com.mikitellurium.turtlecharginstation.util.FastLoc;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FastLoc.modId());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
