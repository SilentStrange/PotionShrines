package com.dreu.potionshrines.registry;

import com.dreu.potionshrines.PotionShrines;
import com.dreu.potionshrines.blocks.ShrineBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSBlockEntities {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PotionShrines.MODID);

    public static final RegistryObject<BlockEntityType<ShrineBlockEntity>> SHRINE =
            BLOCK_ENTITIES.register("shrine",
                    () -> BlockEntityType.Builder.of(ShrineBlockEntity::new, PSBlocks.SHRINE.get()).build(null));
}
