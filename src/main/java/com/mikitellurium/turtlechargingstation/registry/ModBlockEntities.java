package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.BlockEntityRegistrator;
import com.mikitellurium.telluriumforge.registry.deferred.DeferredBlockEntityType;
import com.mikitellurium.turtlechargingstation.TurtleChargingStationMod;
import com.mikitellurium.turtlechargingstation.common.blockentity.CopperCableBlockEntity;
import com.mikitellurium.turtlechargingstation.common.blockentity.ThunderchargeDynamoBlockEntity;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static BlockEntityRegistrator REGISTRATOR;
    public static final DeferredBlockEntityType<BlockEntityType<TurtleChargingStationBlockEntity>> TURTLE_CHARGING_STATION;
    public static final DeferredBlockEntityType<BlockEntityType<ThunderchargeDynamoBlockEntity>> THUNDERCHARGE_DYNAMO;
    public static final DeferredBlockEntityType<BlockEntityType<CopperCableBlockEntity>> COPPER_CABLE;

    static {
        REGISTRATOR = BlockEntityRegistrator.makeRegistrator(TurtleChargingStationMod.modId());
        TURTLE_CHARGING_STATION = REGISTRATOR.ofBlock(TurtleChargingStationBlockEntity::new, ModBlocks.TURTLE_CHARGING_STATION);
        THUNDERCHARGE_DYNAMO = REGISTRATOR.ofBlock(ThunderchargeDynamoBlockEntity::new, ModBlocks.THUNDERCHARGE_DYNAMO);
        COPPER_CABLE = REGISTRATOR.ofBlock(CopperCableBlockEntity::new, ModBlocks.COPPER_CABLE);
    }
}
