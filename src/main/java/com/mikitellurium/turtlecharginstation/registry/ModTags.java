package com.mikitellurium.turtlecharginstation.registry;

import com.mikitellurium.turtlecharginstation.util.FastLoc;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ModTags {
    public static final TagKey<EntityType<?>> DYNAMO_ACTIVATORS = TagKey.create(Registries.ENTITY_TYPE, FastLoc.ofMod("dynamo_activators"));
}
