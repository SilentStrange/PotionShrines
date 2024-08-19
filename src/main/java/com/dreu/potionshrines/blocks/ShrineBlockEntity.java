package com.dreu.potionshrines.blocks;

import com.dreu.potionshrines.registry.PSBlockEntities;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static com.dreu.potionshrines.config.PSShrineConfig.getRandomShrine;

public class ShrineBlockEntity extends BlockEntity {
    public int maxCooldown = 0;
    public String effect = "";
    public int duration = 0;
    public int amplifier = 0;
    private int remainingCooldown = 0;
    public ShrineBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PSBlockEntities.SHRINE.get(), blockPos, blockState);
        Config SHRINE = getRandomShrine();
        amplifier = (int) SHRINE.get("Amplifier") - 1;
        duration = SHRINE.get("Duration");
        maxCooldown = (int) SHRINE.get("Cooldown") * 20;
        effect = SHRINE.get("Effect");
    }
    public static void tick(Level level, BlockPos blockPos, BlockState blockState, ShrineBlockEntity shrineBlockEntity) {
        if (shrineBlockEntity.remainingCooldown > 0) {
            shrineBlockEntity.remainingCooldown--;
            setChanged(level, blockPos, blockState);
        }
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

    @Override
    public void load(CompoundTag nbt){
        super.load(nbt);
        amplifier = nbt.getInt("amplifier");
        remainingCooldown = nbt.getInt("remaining_cooldown");
        maxCooldown = nbt.getInt("max_cooldown");
        duration = nbt.getInt("duration");
        effect = nbt.getString("effect");
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
