package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.BlockEntityRegistrator;
import com.mikitellurium.turtlechargingstation.TurtleChargingStationMod;
import com.mikitellurium.turtlechargingstation.common.blockentity.CopperCableBlockEntity;
import com.mikitellurium.turtlechargingstation.common.blockentity.ThunderchargeDynamoBlockEntity;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final BlockEntityRegistrator REGISTRATOR;
    public static final RegistryObject<BlockEntityType<TurtleChargingStationBlockEntity>> TURTLE_CHARGING_STATION;
    public static final RegistryObject<BlockEntityType<ThunderchargeDynamoBlockEntity>> THUNDERCHARGE_DYNAMO;
    public static final RegistryObject<BlockEntityType<CopperCableBlockEntity>> COPPER_CABLE;

    static {
        REGISTRATOR = BlockEntityRegistrator.makeRegistrator(TurtleChargingStationMod.MOD_ID);
        TURTLE_CHARGING_STATION = REGISTRATOR.ofBlock(TurtleChargingStationBlockEntity::new, ModBlocks.TURTLE_CHARGING_STATION);
        THUNDERCHARGE_DYNAMO = REGISTRATOR.ofBlock(ThunderchargeDynamoBlockEntity::new, ModBlocks.THUNDERCHARGE_DYNAMO);
        COPPER_CABLE = REGISTRATOR.ofBlock(CopperCableBlockEntity::new, ModBlocks.COPPER_CABLE);
    }
}
