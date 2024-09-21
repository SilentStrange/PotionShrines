package com.dreu.potionshrines.screen.aura;

import com.dreu.potionshrines.blocks.shrine.aoe.AoEShrineBlockEntity;
import com.dreu.potionshrines.blocks.shrine.aura.AuraShrineBlockEntity;
import com.dreu.potionshrines.registry.PSMenuTypes;
import com.dreu.potionshrines.screen.ShrineMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AuraShrineMenu extends AbstractContainerMenu implements ShrineMenu {
    public final AuraShrineBlockEntity shrineEntity;
    public AuraShrineMenu(int id, Inventory inventory, FriendlyByteBuf extraData){
        this(id, inventory.player.level.getBlockEntity(extraData.readBlockPos()));
    }
    public AuraShrineMenu(int id, BlockEntity blockEntity){
        super(PSMenuTypes.AURA_SHRINE_MENU.get() , id);
        shrineEntity = (AuraShrineBlockEntity) blockEntity;
    }
    @Override
    public void resetCooldown() {
        shrineEntity.setRemainingCooldown(0);
        shrineEntity.setChanged();
    }
    @Override public ItemStack quickMoveStack(Player player, int i) {return null;}
    @Override public boolean stillValid(Player player) {return true;}
}
