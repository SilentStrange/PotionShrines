package com.dreu.potionshrines.blocks;

import com.dreu.potionshrines.registry.PSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ShrineBlockEntity extends BlockEntity {
    public static int cooldown = 0;
    public static String effect = "";
    public static int duration = 0;
    public static int amplifier = 0;
    public ShrineBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PSBlockEntities.SHRINE.get(), blockPos, blockState);
    }
    public static void tick(Level level, BlockPos blockPos, BlockState blockState, ShrineBlockEntity shrineBlockEntity) {
        if (cooldown > 0) cooldown--;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putString("effect", effect);
        nbt.putInt("duration", duration);
        nbt.putInt("cooldown", cooldown);
        nbt.putInt("amplifier", amplifier);
        super.saveAdditional(nbt);
    }

    public void load(CompoundTag nbt){
        super.load(nbt);
        amplifier = nbt.getInt("amplifier");
        cooldown = nbt.getInt("cooldown");
        duration = nbt.getInt("duration");
        effect = nbt.getString("effect");
    }
}
