package com.dreu.potionshrines.blocks.shrine.simple;

import com.dreu.potionshrines.registry.PSBlockEntities;
import com.dreu.potionshrines.registry.PSBlocks;
import com.dreu.potionshrines.screen.simple.SimpleShrineMenu;
import com.electronwill.nightconfig.core.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.dreu.potionshrines.blocks.shrine.simple.SimpleShrineBlock.LIGHT_LEVEL;
import static com.dreu.potionshrines.config.General.SHRINES_REPLENISH;
import static com.dreu.potionshrines.config.SimpleShrine.getRandomShrine;

public class SimpleShrineBlockEntity extends BlockEntity implements MenuProvider {
    private int maxCooldown = 0, duration = 0, amplifier = 1, remainingCooldown = 0;
    private String effect = "null", icon = "default";
    private boolean replenish = true;
    public SimpleShrineBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PSBlockEntities.SIMPLE_SHRINE.get(), blockPos, blockState);
    }

     public SimpleShrineBlockEntity fromConfig() {
        Config shrine = getRandomShrine();
        setAmplifier(shrine.getInt("Amplifier") - 1);
        setDuration(shrine.getInt("Duration") * 20);
        setMaxCooldown(shrine.getInt("Cooldown") * 20);
        setCanReplenish(shrine.get("Replenish"));
        setEffect(shrine.get("Effect"));
        setIcon(shrine.get("Icon"));
        return this;
    }


    public static void tick(Level level, BlockPos blockPos, BlockState blockState, SimpleShrineBlockEntity shrine) {
        if (!Objects.equals(shrine.effect, "null")) {
            if (shrine.remainingCooldown > 30) {
                if (shrine.remainingCooldown == 40)
                    level.playSound(null, blockPos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 3F, 1F);
                if (shrine.remainingCooldown > shrine.maxCooldown - 29) {
                    level.setBlock(blockPos, blockState.setValue(LIGHT_LEVEL, 15 - (shrine.maxCooldown - shrine.remainingCooldown) / 2), 11);
                } else if (!SHRINES_REPLENISH || !shrine.replenish) {
                    level.removeBlock(blockPos.below(2), false);
                    level.removeBlock(blockPos.below(1), false);
                    level.removeBlock(blockPos, false);
                    level.setBlock(blockPos.below(2), PSBlocks.SHRINE_DECREPIT.get().defaultBlockState(), 11);
                }
                shrine.setRemainingCooldown(shrine.remainingCooldown - 1);
                setChanged(level, blockPos, blockState);
            } else if (shrine.remainingCooldown > 0) {
                shrine.setRemainingCooldown(shrine.remainingCooldown - 1);
                setChanged(level, blockPos, blockState);
                level.setBlock(blockPos, blockState.setValue(LIGHT_LEVEL, 15 - shrine.remainingCooldown / 2), 11);
            } else if (!level.isClientSide)
                level.setBlock(blockPos, blockState.setValue(LIGHT_LEVEL, 15), 11);
        }
    }
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putString("effect", effect);
        nbt.putInt("duration", duration);
        nbt.putInt("max_cooldown", maxCooldown);
        nbt.putInt("remaining_cooldown", remainingCooldown);
        nbt.putInt("amplifier", amplifier);
        nbt.putBoolean("replenish", replenish);
        nbt.putString("icon", icon);
        super.saveAdditional(nbt);
    }
    @Override
    public void load(CompoundTag nbt){
        super.load(nbt);
        amplifier = nbt.getInt("amplifier");
        remainingCooldown = nbt.getInt("remaining_cooldown");
        maxCooldown = nbt.getInt("max_cooldown");
        duration = nbt.getInt("duration");
        effect = nbt.getString("effect");
        replenish = nbt.getBoolean("replenish");
        icon = nbt.getString("icon");
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        CompoundTag nbt = new CompoundTag();
        this.saveAdditional(nbt);
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        this.saveAdditional(nbt);
        return nbt;
    }
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    public String getEffect(){return effect;}
    public int getDuration(){return duration;}
    public int getMaxCooldown(){return maxCooldown;}
    public int getRemainingCooldown(){return remainingCooldown;}
    public int getAmplifier(){return amplifier;}
    public boolean canReplenish() {return replenish;}

    public void setEffect(String resourceLocation){effect = resourceLocation;}
    public void setAmplifier(int lvl){amplifier = Mth.clamp(lvl, 1, 256);}
    public void setDuration(int ticks){duration = Mth.clamp(ticks, 1, 19999980);}
    public void setMaxCooldown(int ticks){
        maxCooldown = Mth.clamp(ticks, 60, 19999980);
        if (remainingCooldown > maxCooldown) remainingCooldown = maxCooldown;
    }
    public void setRemainingCooldown(int ticks){
        remainingCooldown = Mth.clamp(ticks, 0, maxCooldown);
    }
    public void setCanReplenish(boolean b){replenish = b;}
    public void setIcon(String name){icon = name;}
    public String getIcon(){return icon;}
    public boolean canUse() {
        return remainingCooldown == 0;
    }
    public void resetCooldown(){
        remainingCooldown = maxCooldown;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.potion_shrines.shrine_options");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new SimpleShrineMenu(id, this);
    }
}
