package com.dreu.potionshrines.blocks;

import com.dreu.potionshrines.registry.PSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ShrineBlockEntity extends BlockEntity {
    public int maxCooldown = 0;
    public String effect = "";
    public int duration = 0;
    public int amplifier = 0;
    private int remainingCooldown = 0;
    public ShrineBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PSBlockEntities.SHRINE.get(), blockPos, blockState);
    }
    public static void tick(Level level, BlockPos blockPos, BlockState blockState, ShrineBlockEntity shrineBlockEntity) {
        if (shrineBlockEntity.remainingCooldown > 0) shrineBlockEntity.remainingCooldown--;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putString("effect", effect);
        nbt.putInt("duration", duration);
        nbt.putInt("max_cooldown", maxCooldown);
        nbt.putInt("remaining_cooldown", remainingCooldown);
        nbt.putInt("amplifier", amplifier);
        super.saveAdditional(nbt);
    }

    public void load(CompoundTag nbt){
        super.load(nbt);
        amplifier = nbt.getInt("amplifier");
        remainingCooldown = nbt.getInt("remaining_cooldown");
        maxCooldown = nbt.getInt("max_cooldown");
        duration = nbt.getInt("duration");
        effect = nbt.getString("effect");
    }

    public String getEffect(){return effect;}
    public int getDuration(){return duration;}
    public int getMaxCooldown(){return maxCooldown;}
    public int getRemainingCooldown(){return remainingCooldown;}
    public int getAmplifier(){return amplifier;}
    public void setEffect(String eff){effect = eff;}
    public void setDuration(int dur){duration = dur;}
    public void setMaxCooldown(int cool){maxCooldown = cool;}
    public void setAmplifier(int amp){amplifier = amp;}

    public boolean canUse() {
        return remainingCooldown == 0;
    }
    public void resetCooldown(){
        remainingCooldown = maxCooldown;
    }
}
