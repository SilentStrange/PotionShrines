package com.dreu.potionshrines.registry;

import com.dreu.potionshrines.PotionShrines;
import com.dreu.potionshrines.blocks.shrine.aoe.AoEShrineBlockEntity;
import com.dreu.potionshrines.blocks.shrine.simple.SimpleShrineBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSBlockEntities {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PotionShrines.MODID);

    public static final RegistryObject<BlockEntityType<SimpleShrineBlockEntity>> SIMPLE_SHRINE =
            BLOCK_ENTITIES.register("shrine",
                    () -> BlockEntityType.Builder.of(SimpleShrineBlockEntity::new, PSBlocks.SHRINE.get()).build(null));
        public static final RegistryObject<BlockEntityType<AoEShrineBlockEntity>> AOE_SHRINE =
            BLOCK_ENTITIES.register("aoe_shrine",
                    () -> BlockEntityType.Builder.of(AoEShrineBlockEntity::new, PSBlocks.AOE_SHRINE.get()).build(null));
}
