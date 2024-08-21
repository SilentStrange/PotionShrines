package com.dreu.potionshrines.blocks;

import com.dreu.potionshrines.registry.PSBlockEntities;
import com.electronwill.nightconfig.core.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static com.dreu.potionshrines.blocks.ShrineBlock.LIGHT_LEVEL;
import static com.dreu.potionshrines.config.PSGeneralConfig.SHRINES_REPLENISH;
import static com.dreu.potionshrines.config.PSShrineConfig.getRandomShrine;

public class ShrineBlockEntity extends BlockEntity {
    public int maxCooldown;
    public String effect;
    public int duration;
    public int amplifier;
    private int remainingCooldown = 0;
    private String icon;
    public ShrineBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PSBlockEntities.SHRINE.get(), blockPos, blockState);
        Config SHRINE = getRandomShrine();
        amplifier = (int) SHRINE.get("Amplifier") - 1;
        duration = SHRINE.get("Duration");
        maxCooldown = (int) SHRINE.get("Cooldown") * 20;
        effect = SHRINE.get("Effect");
        icon = SHRINE.get("Icon");
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, ShrineBlockEntity shrine) {
        if (shrine.maxCooldown == 999999) return;
        if (shrine.remainingCooldown == 30)
            level.playLocalSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 10F, 1F, true);
        if (SHRINES_REPLENISH && shrine.remainingCooldown > 30) {
            if (shrine.remainingCooldown > shrine.maxCooldown - 30){
                level.setBlock(blockPos, blockState.setValue(LIGHT_LEVEL, 15 - (shrine.maxCooldown - shrine.remainingCooldown) / 2), 11);
            }
            shrine.remainingCooldown--;
            setChanged(level, blockPos, blockState);
        } else if (shrine.remainingCooldown > 0){
            shrine.remainingCooldown--;
            level.setBlock(blockPos, blockState.setValue(LIGHT_LEVEL, 15 - shrine.remainingCooldown / 2), 11);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putString("effect", effect);
        nbt.putInt("duration", duration);
        nbt.putInt("max_cooldown", maxCooldown);
        nbt.putInt("remaining_cooldown", remainingCooldown);
        nbt.putInt("amplifier", amplifier);
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
    public String getIcon(){return icon;}
    public boolean canUse() {
        return remainingCooldown == 0;
    }
    public void resetCooldown(){
        remainingCooldown = maxCooldown;
    }
}
