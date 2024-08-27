package com.dreu.potionshrines.registry;

import com.dreu.potionshrines.items.AoEShrineBlockItem;
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
        public static final RegistryObject<Item> SHRINE = ITEMS.register("shrine", () -> new ShrineBlockItem(PSBlocks.SHRINE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
        public static final RegistryObject<Item> SHRINE_DECREPIT = ITEMS.register("shrine_decrepit", () -> new BlockItem(PSBlocks.SHRINE_DECREPIT.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

        public static final RegistryObject<Item> AOE_SHRINE = ITEMS.register("aoe_shrine", () -> new AoEShrineBlockItem(PSBlocks.AOE_SHRINE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
        public static final RegistryObject<Item> AOE_SHRINE_DECREPIT = ITEMS.register("aoe_shrine_decrepit", () -> new BlockItem(PSBlocks.AOE_SHRINE_DECREPIT.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
}
