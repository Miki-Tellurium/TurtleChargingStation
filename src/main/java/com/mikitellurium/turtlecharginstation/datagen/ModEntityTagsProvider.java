package com.mikitellurium.turtlecharginstation.datagen;

import com.mikitellurium.turtlecharginstation.registry.ModTags;
import com.mikitellurium.turtlecharginstation.util.FastLoc;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import weather2.EntityRegistry;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModEntityTagsProvider extends EntityTypeTagsProvider {

    public ModEntityTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, FastLoc.modId(), existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.DYNAMO_ACTIVATORS)
                .add(EntityType.LIGHTNING_BOLT)
                .addOptional(EntityRegistry.LIGHTNING_BOLT.getId());
    }

}
