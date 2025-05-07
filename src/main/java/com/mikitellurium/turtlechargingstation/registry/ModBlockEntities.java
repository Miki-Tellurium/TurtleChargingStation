package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.telluriumforge.registry.BlockEntityRegistrator;
import com.mikitellurium.telluriumforge.registry.Registrator;
import com.mikitellurium.turtlechargingstation.common.blockentity.CopperCableBlockEntity;
import com.mikitellurium.turtlechargingstation.common.blockentity.ThunderchargeDynamoBlockEntity;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlechargingstation.test.EnergyTestBlockEntity;
import com.mikitellurium.turtlechargingstation.util.FastId;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import team.reborn.energy.api.EnergyStorage;

public class ModBlockEntities {
    public static final BlockEntityRegistrator REGISTRATOR;
    public static final BlockEntityType<TurtleChargingStationBlockEntity> TURTLE_CHARGING_STATION;
    public static final BlockEntityType<ThunderchargeDynamoBlockEntity> THUNDERCHARGE_DYNAMO;
    public static final BlockEntityType<CopperCableBlockEntity> COPPER_CABLE;

    static {
        REGISTRATOR = new BlockEntityRegistrator(FastId.modId());
        TURTLE_CHARGING_STATION = REGISTRATOR.ofBlock("turtle_charging_station", TurtleChargingStationBlockEntity::new, ModBlocks.TURTLE_CHARGING_STATION);
        THUNDERCHARGE_DYNAMO = REGISTRATOR.ofBlock("thundercharge_dynamo", ThunderchargeDynamoBlockEntity::new, ModBlocks.THUNDERCHARGE_DYNAMO);
        COPPER_CABLE = REGISTRATOR.ofBlock("copper_cable", CopperCableBlockEntity::new, ModBlocks.COPPER_CABLE);
        EnergyStorage.SIDED.registerForBlockEntity(TurtleChargingStationBlockEntity::energyLookup, TURTLE_CHARGING_STATION);
        ItemStorage.SIDED.registerForBlockEntity(TurtleChargingStationBlockEntity::inventoryLookup, TURTLE_CHARGING_STATION);
        EnergyStorage.SIDED.registerForBlockEntity(ThunderchargeDynamoBlockEntity::energyLookup, THUNDERCHARGE_DYNAMO);
        PeripheralLookup.get().registerForBlockEntity(ThunderchargeDynamoBlockEntity::peripheralLookup, THUNDERCHARGE_DYNAMO);
        EnergyStorage.SIDED.registerForBlockEntity(CopperCableBlockEntity::energyLookup, COPPER_CABLE);
    }
}
