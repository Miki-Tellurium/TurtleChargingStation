package com.mikitellurium.turtlecharginstation.registry;

import com.mikitellurium.turtlecharginstation.block.ThunderchargeDynamoBlock;
import com.mikitellurium.turtlecharginstation.block.TurtleChargingStationBlock;
import com.mikitellurium.turtlecharginstation.util.FastLoc;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FastLoc.modId());

    public static final RegistryObject<Block> TURTLE_CHARGING_STATION_BLOCK = registerBlock("turtle_charging_station",
            TurtleChargingStationBlock::new);

    public static final RegistryObject<Block> THUNDERCHARGE_DYNAMO_BLOCK = registerBlock("thundercharge_dynamo",
            ThunderchargeDynamoBlock::new);

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    public static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
