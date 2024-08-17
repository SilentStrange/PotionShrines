package com.dreu.potionshrines.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.dreu.potionshrines.PotionShrines.MODID;
import static com.dreu.potionshrines.registry.PSBlocks.POTION_SHRINE;

public class PSItems {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

        public static final RegistryObject<Item> POTION_SHRINE_ITEM = ITEMS.register("potion_shrine", () -> new BlockItem(POTION_SHRINE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

}
