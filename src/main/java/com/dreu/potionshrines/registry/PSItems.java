package com.dreu.potionshrines.registry;

import com.dreu.potionshrines.items.ShrineBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.dreu.potionshrines.PotionShrines.MODID;

public class PSItems {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

        public static final RegistryObject<Item> SHRINE = ITEMS.register("simple_shrine", () -> new ShrineBlockItem(PSBlocks.SIMPLE_SHRINE.get(), PSBlocks.SIMPLE_SHRINE_BASE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
        public static final RegistryObject<Item> SHRINE_DECREPIT = ITEMS.register("simple_shrine_decrepit", () -> new BlockItem(PSBlocks.SIMPLE_SHRINE_DECREPIT.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

        public static final RegistryObject<Item> AOE_SHRINE = ITEMS.register("aoe_shrine", () -> new ShrineBlockItem(PSBlocks.AOE_SHRINE.get(), PSBlocks.AOE_SHRINE_BASE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
        public static final RegistryObject<Item> AOE_SHRINE_DECREPIT = ITEMS.register("aoe_shrine_decrepit", () -> new BlockItem(PSBlocks.AOE_SHRINE_DECREPIT.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

        public static final RegistryObject<Item> AURA_SHRINE = ITEMS.register("aura_shrine", () -> new ShrineBlockItem(PSBlocks.AURA_SHRINE.get(), PSBlocks.AURA_SHRINE_BASE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
        public static final RegistryObject<Item> AURA_SHRINE_DECREPIT = ITEMS.register("aura_shrine_decrepit", () -> new BlockItem(PSBlocks.AURA_SHRINE_DECREPIT.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
}
