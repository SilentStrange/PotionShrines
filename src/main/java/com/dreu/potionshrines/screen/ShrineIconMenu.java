package com.dreu.potionshrines.screen;

import com.dreu.potionshrines.registry.PSMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class ShrineIconMenu extends AbstractContainerMenu {
    public ShrineIconMenu(int id, Inventory inventory, FriendlyByteBuf extraData){
        this(id);
    }
    public ShrineIconMenu(int id){
        super(PSMenuTypes.SHRINE_ICON_MENU.get() , id);
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
