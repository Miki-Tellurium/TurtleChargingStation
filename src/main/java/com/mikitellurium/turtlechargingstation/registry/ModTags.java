package com.mikitellurium.turtlechargingstation.registry;

import com.mikitellurium.turtlechargingstation.util.FastId;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {

    public static final TagKey<EntityType<?>> DYNAMO_ACTIVATORS = TagKey.of(RegistryKeys.ENTITY_TYPE, FastId.ofMod("dynamo_activators"));

}
