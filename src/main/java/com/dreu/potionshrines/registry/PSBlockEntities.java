package com.dreu.potionshrines.registry;

import com.dreu.potionshrines.PotionShrines;
import com.dreu.potionshrines.blocks.shrine.aoe.AoEShrineBlockEntity;
import com.dreu.potionshrines.blocks.shrine.aura.AuraShrineBlockEntity;
import com.dreu.potionshrines.blocks.shrine.simple.SimpleShrineBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
@SuppressWarnings("all")
public class PSBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PotionShrines.MODID);
    public static final RegistryObject<BlockEntityType<SimpleShrineBlockEntity>> SIMPLE_SHRINE =
            BLOCK_ENTITIES.register("shrine",
                    () -> BlockEntityType.Builder.of(SimpleShrineBlockEntity::new, PSBlocks.SIMPLE_SHRINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<AoEShrineBlockEntity>> AOE_SHRINE =
            BLOCK_ENTITIES.register("aoe_shrine",
                    () -> BlockEntityType.Builder.of(AoEShrineBlockEntity::new, PSBlocks.AOE_SHRINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<AuraShrineBlockEntity>> AURA_SHRINE =
            BLOCK_ENTITIES.register("aura_shrine",
                    () -> BlockEntityType.Builder.of(AuraShrineBlockEntity::new, PSBlocks.AURA_SHRINE.get()).build(null));
}
