package com.dreu.potionshrines.registry;

import com.dreu.potionshrines.PotionShrines;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;


public class PSTags {
    public static TagKey<EntityType<?>> entityTypeTagKey(String modid, String name) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(modid, name));
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> MONSTERS = entityTypeTagKey(PotionShrines.MODID, "considered_monster");
    }
}
