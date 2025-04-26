package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.turtlechargingstation.TurtleChargingStationMod;
import com.mikitellurium.turtlechargingstation.block.ThunderchargeDynamoBlock;
import com.mikitellurium.turtlechargingstation.block.TurtleChargingStationBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {

    public static DeferredRegister<Block> BLOCKS =
            DeferredRegister.createBlocks(TurtleChargingStationMod.MOD_ID);

    public static final DeferredBlock<TurtleChargingStationBlock> TURTLE_CHARGING_STATION_BLOCK = registerBlock("turtle_charging_station",
            TurtleChargingStationBlock::new);

    public static final DeferredBlock<ThunderchargeDynamoBlock> THUNDERCHARGE_DYNAMO_BLOCK = registerBlock("thundercharge_dynamo",
            ThunderchargeDynamoBlock::new);

    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredHolder<Block, T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return (DeferredBlock<T>) toReturn;
    }

    public static <T extends Block> DeferredItem<BlockItem> registerBlockItem(String name, DeferredHolder<Block, T> block) {
        DeferredHolder<Item, BlockItem> toReturn = ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return (DeferredItem<BlockItem>) toReturn;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
