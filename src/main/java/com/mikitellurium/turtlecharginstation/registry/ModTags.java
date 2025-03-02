package com.mikitellurium.turtlecharginstation.registry;

import com.mikitellurium.turtlecharginstation.util.FastLoc;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static final TagKey<Block> DYNAMO_CONDUCTIVE_BLOCKS = BlockTags.create(FastLoc.modLoc("dynamo_conductive_blocks"));
    public static final TagKey<EntityType<?>> DYNAMO_ACTIVATORS = TagKey.create(Registries.ENTITY_TYPE, FastLoc.modLoc("dynamo_activators"));

}
