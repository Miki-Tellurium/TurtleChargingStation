package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.RegistryHelper;
import com.mikitellurium.turtlechargingstation.common.blockentity.CopperCableBlockEntity;
import com.mikitellurium.turtlechargingstation.common.blockentity.ThunderchargeDynamoBlockEntity;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Set;
import java.util.function.BiFunction;

public class ModBlockEntities {

    public static RegistryHelper<BlockEntityType<?>> REGISTRY;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurtleChargingStationBlockEntity>> TURTLE_CHARGING_STATION;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ThunderchargeDynamoBlockEntity>> THUNDERCHARGE_DYNAMO;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CopperCableBlockEntity>> COPPER_CABLE;

    static {
        REGISTRY = ModRegistries.makeRegistry(Registries.BLOCK_ENTITY_TYPE);
        TURTLE_CHARGING_STATION = ofBlock(ModBlocks.TURTLE_CHARGING_STATION, TurtleChargingStationBlockEntity::new);
        THUNDERCHARGE_DYNAMO = ofBlock(ModBlocks.THUNDERCHARGE_DYNAMO, ThunderchargeDynamoBlockEntity::new);
        COPPER_CABLE = ofBlock(ModBlocks.COPPER_CABLE, CopperCableBlockEntity::new);
    }

    public static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> ofBlock(DeferredBlock<Block> block, BiFunction<BlockPos, BlockState, T> factory) {
        return REGISTRY.register(block.getId().getPath(), () -> new BlockEntityType<>(factory::apply, Set.of(block.get()), null));
    }

}
