package com.mikitellurium.turtlecharginstation.registry;

import com.mikitellurium.telluriumforge.registry.RegistryHelper;
import com.mikitellurium.turtlecharginstation.blockentity.CopperCableBlockEntity;
import com.mikitellurium.turtlecharginstation.blockentity.ThunderchargeDynamoBlockEntity;
import com.mikitellurium.turtlecharginstation.blockentity.TurtleChargingStationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
import java.util.function.BiFunction;

public class ModBlockEntities {

    public static RegistryHelper<BlockEntityType<?>> REGISTRY;
    public static final RegistryObject<BlockEntityType<TurtleChargingStationBlockEntity>> TURTLE_CHARGING_STATION;
    public static final RegistryObject<BlockEntityType<ThunderchargeDynamoBlockEntity>> THUNDERCHARGE_DYNAMO;
    public static final RegistryObject<BlockEntityType<CopperCableBlockEntity>> COPPER_CABLE;

    static {
        REGISTRY = ModRegistries.makeRegistry(ForgeRegistries.BLOCK_ENTITY_TYPES);
        TURTLE_CHARGING_STATION = ofBlock(ModBlocks.TURTLE_CHARGING_STATION, TurtleChargingStationBlockEntity::new);
        THUNDERCHARGE_DYNAMO = ofBlock(ModBlocks.THUNDERCHARGE_DYNAMO, ThunderchargeDynamoBlockEntity::new);
        COPPER_CABLE = ofBlock(ModBlocks.COPPER_CABLE, CopperCableBlockEntity::new);
    }

    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> ofBlock(RegistryObject<Block> block, BiFunction<BlockPos, BlockState, T> factory) {
        return REGISTRY.register(block.getId().getPath(), () -> new BlockEntityType<>(factory::apply, Set.of(block.get()), null));
    }

}
