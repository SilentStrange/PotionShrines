package com.dreu.potionshrines.screen;

import com.dreu.potionshrines.registry.PSMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class IconSelectionMenu extends AbstractContainerMenu {
    public IconSelectionMenu(int id, Inventory inventory, FriendlyByteBuf extraData){
        this(id);
    }
    public IconSelectionMenu(int id){
        super(PSMenuTypes.ICON_SELECTION_MENU.get() , id);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
