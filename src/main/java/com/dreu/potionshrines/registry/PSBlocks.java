package com.dreu.potionshrines.registry;

import com.dreu.potionshrines.blocks.shrine.aoe.AoEDecrepitBlock;
import com.dreu.potionshrines.blocks.shrine.aoe.AoEShrineBaseBlock;
import com.dreu.potionshrines.blocks.shrine.aoe.AoEShrineBlock;
import com.dreu.potionshrines.blocks.shrine.simple.ShrineBaseBlock;
import com.dreu.potionshrines.blocks.shrine.simple.ShrineBlock;
import com.dreu.potionshrines.blocks.shrine.simple.ShrineDecrepitBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.dreu.potionshrines.PotionShrines.MODID;
import static com.dreu.potionshrines.blocks.shrine.simple.ShrineBlock.LIGHT_LEVEL;
import static com.dreu.potionshrines.config.General.SHRINE_INDESTRUCTIBLE;

public class PSBlocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

        public static final RegistryObject<Block> SHRINE = BLOCKS.register("shrine",
                () -> new ShrineBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .lightLevel((blockstate) -> blockstate.getValue(LIGHT_LEVEL))
                        .emissiveRendering((blockState, blockGetter, blockPos) -> true)));
        public static final RegistryObject<Block> SHRINE_BASE = BLOCKS.register("shrine_base",
                () -> new ShrineBaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .strength(SHRINE_INDESTRUCTIBLE ? -1 : 5)));
        public static final RegistryObject<Block> SHRINE_DECREPIT = BLOCKS.register("shrine_decrepit",
                () -> new ShrineDecrepitBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .strength(5)));

                public static final RegistryObject<Block> AOE_SHRINE = BLOCKS.register("aoe_shrine",
                () -> new AoEShrineBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .lightLevel((blockstate) -> blockstate.getValue(LIGHT_LEVEL))
                        .emissiveRendering((blockState, blockGetter, blockPos) -> true)));
        public static final RegistryObject<Block> AOE_SHRINE_BASE = BLOCKS.register("aoe_shrine_base",
                () -> new AoEShrineBaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .strength(SHRINE_INDESTRUCTIBLE ? -1 : 5)));
        public static final RegistryObject<Block> AOE_SHRINE_DECREPIT = BLOCKS.register("aoe_shrine_decrepit",
                () -> new AoEDecrepitBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .strength(5)));
}
