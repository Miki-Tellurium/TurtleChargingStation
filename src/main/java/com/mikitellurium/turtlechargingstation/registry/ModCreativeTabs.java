package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ModCreativeTabs {
    public static final CreativeTabs MAIN_TAB = new CreativeTabs(FastLoc.modId()) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.TURTLE_CHARGING_STATION);
        }

        @Override
        public String getTranslationKey() {
            return "itemGroup.main.name";
        }
    };
}
