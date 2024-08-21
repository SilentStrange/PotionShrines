package com.dreu.potionshrines.registry;

import com.dreu.potionshrines.blocks.ShrineBaseBlock;
import com.dreu.potionshrines.blocks.ShrineBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.dreu.potionshrines.PotionShrines.MODID;
import static com.dreu.potionshrines.blocks.ShrineBlock.LIGHT_LEVEL;

public class PSBlocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
        public static final RegistryObject<Block> SHRINE = BLOCKS.register("shrine", () -> new ShrineBlock(BlockBehaviour.Properties.copy(Blocks.STONE).lightLevel((blockstate) -> blockstate.getValue(LIGHT_LEVEL)).emissiveRendering((blockState, blockGetter, blockPos) -> true)));
        public static final RegistryObject<Block> SHRINE_BASE = BLOCKS.register("shrine_base", () -> new ShrineBaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
}
