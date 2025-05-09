package com.mikitellurium.turtlechargingstation.common.capability;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class PeripheralProvider<B extends BlockEntity> implements ICapabilityProvider {

    public static Capability<IPeripheral> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    private IPeripheral peripheral = null;
    private final LazyOptional<IPeripheral> holder = LazyOptional.of(this::create);
    private final B blockEntity;
    private final Function<B, IPeripheral> factory;

    public PeripheralProvider(B blockEntity, Function<B, IPeripheral> factory) {
        this.blockEntity = blockEntity;
        this.factory = factory;
    }

    private IPeripheral create() {
        if (peripheral == null) {
            peripheral = factory.apply(blockEntity);
        }
        return peripheral;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == INSTANCE && !this.blockEntity.isRemoved()) {
            return holder.cast();
        }
        return LazyOptional.empty();
    }

}
