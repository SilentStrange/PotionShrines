package com.dreu.potionshrines.screen;

import com.dreu.potionshrines.blocks.aoe.AoEShrineBlockEntity;
import com.dreu.potionshrines.registry.PSMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AoEShrineMenu extends AbstractContainerMenu {
    public final AoEShrineBlockEntity shrineEntity;
    private final Level level;
    public AoEShrineMenu(int id, Inventory inventory, FriendlyByteBuf extraData){
        this(id, inventory, inventory.player.level.getBlockEntity(extraData.readBlockPos()));
    }
    public AoEShrineMenu(int id, Inventory inventory, BlockEntity blockEntity){
        super(PSMenuTypes.AOE_SHRINE_MENU.get() , id);
        shrineEntity = (AoEShrineBlockEntity) blockEntity;
        this.level = inventory.player.level;
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
