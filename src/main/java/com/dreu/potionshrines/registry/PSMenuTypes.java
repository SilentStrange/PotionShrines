package com.dreu.potionshrines.registry;

import com.dreu.potionshrines.screen.IconSelectionMenu;
import com.dreu.potionshrines.screen.aoe.AoEShrineMenu;
import com.dreu.potionshrines.screen.aura.AuraShrineMenu;
import com.dreu.potionshrines.screen.simple.SimpleShrineMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.dreu.potionshrines.PotionShrines.MODID;

public class PSMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);

    public static final RegistryObject<MenuType<SimpleShrineMenu>> SIMPLE_SHRINE_MENU =
            registerMenuType(SimpleShrineMenu::new, "simple_shrine_menu");

    public static final RegistryObject<MenuType<AoEShrineMenu>> AOE_SHRINE_MENU =
            registerMenuType(AoEShrineMenu::new, "aoe_shrine_menu");

    public static final RegistryObject<MenuType<AuraShrineMenu>> AURA_SHRINE_MENU =
            registerMenuType(AuraShrineMenu::new, "aura_shrine_menu");

    public static final RegistryObject<MenuType<IconSelectionMenu>> ICON_SELECTION_MENU =
            registerMenuType(IconSelectionMenu::new, "icon_selection_menu");
    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name){
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }
}
