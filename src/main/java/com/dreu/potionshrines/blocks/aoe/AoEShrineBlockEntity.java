package com.dreu.potionshrines.blocks.aoe;

import com.dreu.potionshrines.registry.PSBlockEntities;
import com.dreu.potionshrines.registry.PSBlocks;
import com.dreu.potionshrines.screen.AoEShrineMenu;
import com.electronwill.nightconfig.core.Config;
import com.mojang.math.Vector3f;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static com.dreu.potionshrines.PotionShrines.getEffectFromString;
import static com.dreu.potionshrines.PotionShrines.rand;
import static com.dreu.potionshrines.blocks.shrine.ShrineBlock.LIGHT_LEVEL;
import static com.dreu.potionshrines.config.AoEShrine.getRandomAoEShrine;
import static com.dreu.potionshrines.config.General.SHRINES_REPLENISH;
import static com.dreu.potionshrines.config.PSConfig.rangeBounded;

public class AoEShrineBlockEntity extends BlockEntity implements MenuProvider {
    public int maxCooldown;
    public String effect;
    public int radius;
    public boolean effectPlayers;
    public boolean effectMonsters;
    public int duration;
    public int amplifier;
    private int remainingCooldown = 0;
    public boolean replenish;
    private String icon;
    public AoEShrineBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PSBlockEntities.AOE_SHRINE.get(), blockPos, blockState);
        Config aoeShrine = getRandomAoEShrine();
        amplifier = rangeBounded((int) aoeShrine.get("Amplifier") - 1, 1, 256);
        duration = rangeBounded(aoeShrine.get("Duration"), 1, 999999);
        maxCooldown = rangeBounded((int) aoeShrine.get("Cooldown") * 20, 3, 999999);
        replenish = aoeShrine.get("Replenish");
        effect = aoeShrine.get("Effect");
        icon = aoeShrine.get("Icon");
        effectPlayers = aoeShrine.get("Players");
        effectMonsters = aoeShrine.get("Monsters");
        radius = rangeBounded(aoeShrine.get("Radius"), 3, 64);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, AoEShrineBlockEntity shrine) {
        if (shrine.remainingCooldown > 30) {
            if (shrine.remainingCooldown == 40)
                level.playSound(null, blockPos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 3F, 1F);
            if (shrine.remainingCooldown > shrine.maxCooldown - 29){
                Vector3f color = new Vector3f(
                        (getEffectFromString(shrine.effect).getColor() >> 16 & 0xFF) / 255.0f,
                        (getEffectFromString(shrine.effect).getColor() >> 8 & 0xFF) / 255.0f,
                        (getEffectFromString(shrine.effect).getColor() & 0xFF) / 255.0f
                );
                if (shrine.remainingCooldown > shrine.maxCooldown - 10) {
                    double iterations = (double) shrine.radius / 10;
                    double radius = (double) (shrine.radius * (shrine.maxCooldown - shrine.remainingCooldown)) / 10;
                    double angleIncrement = Math.PI * (3 + (double) (2 * shrine.radius) / 64) / (shrine.radius * 10);

                    int xPos = shrine.getBlockPos().getX();
                    int yPos = shrine.getBlockPos().getY() - 2;
                    int zPos = shrine.getBlockPos().getZ();

                    for (int rings = 0; rings < iterations; rings++) {
                        for (double angle = 0; angle < 2 * Math.PI; angle += angleIncrement) {
                            double xOffset = rand.nextFloat() + (radius + rings) * Math.cos(angle);
                            double zOffset = rand.nextFloat() + (radius + rings) * Math.sin(angle);

                            level.addParticle(ParticleTypes.ENTITY_EFFECT,
                                xPos + xOffset, yPos, zPos + zOffset,
                                color.x(), color.y(), color.z());
                        }
                    }
                }
                level.setBlock(blockPos, blockState.setValue(LIGHT_LEVEL, 15 - (shrine.maxCooldown - shrine.remainingCooldown) / 2), 11);
            } else if (!SHRINES_REPLENISH || !shrine.replenish){
                level.setBlock(blockPos.below(2), PSBlocks.AOE_SHRINE_DECREPIT.get().defaultBlockState(), 11);
            }
            shrine.remainingCooldown--;
            setChanged(level, blockPos, blockState);
        } else if (shrine.remainingCooldown > 0){
            shrine.remainingCooldown--;
            setChanged(level, blockPos, blockState);
            level.setBlock(blockPos, blockState.setValue(LIGHT_LEVEL, 15 - shrine.remainingCooldown / 2), 11);
        } else {
             if (shrine.getLevel().getGameTime() % 2 == 1) {
                Vector3f color = new Vector3f(
                        (getEffectFromString(shrine.effect).getColor() >> 16 & 0xFF) / 255.0f,
                        (getEffectFromString(shrine.effect).getColor() >> 8 & 0xFF) / 255.0f,
                        (getEffectFromString(shrine.effect).getColor() & 0xFF) / 255.0f
                );
                int particleCount = (int) Math.pow((double) shrine.radius / 5, 3) + 1;
                for (int i = 0; i < particleCount; i++) {
                    double theta = Math.random() * 2 * Math.PI;
                    double phi = Math.acos(2 * Math.random() - 1);
                    shrine.getLevel().addParticle(ParticleTypes.ENTITY_EFFECT,
                        shrine.getBlockPos().getX() + 0.5 + shrine.radius * Math.sin(phi) * Math.cos(theta),
                        shrine.getBlockPos().getY() + 0.5 + shrine.radius * Math.sin(phi) * Math.sin(theta),
                        shrine.getBlockPos().getZ() + 0.5 + shrine.radius * Math.cos(phi),
                        color.x(), color.y(), color.z());
                }
            }
            if (!level.isClientSide)
                level.setBlock(blockPos, blockState.setValue(LIGHT_LEVEL, 15), 11);
        }
    }
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putString("effect", effect);
        nbt.putInt("duration", duration);
        nbt.putInt("max_cooldown", maxCooldown);
        nbt.putBoolean("replenish", replenish);
        nbt.putInt("remaining_cooldown", remainingCooldown);
        nbt.putInt("amplifier", amplifier);
        nbt.putString("icon", icon);
        nbt.putBoolean("players", effectPlayers);
        nbt.putBoolean("monsters", effectMonsters);
        nbt.putInt("radius", radius);
        super.saveAdditional(nbt);
    }
    @Override
    public void load(CompoundTag nbt){
        super.load(nbt);
        amplifier = nbt.getInt("amplifier");
        remainingCooldown = nbt.getInt("remaining_cooldown");
        maxCooldown = nbt.getInt("max_cooldown");
        replenish = nbt.getBoolean("replenish");
        duration = nbt.getInt("duration");
        effect = nbt.getString("effect");
        icon = nbt.getString("icon");
        effectPlayers = nbt.getBoolean("players");
        effectMonsters = nbt.getBoolean("monsters");
        radius = nbt.getInt("radius");
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

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.potion_shrines.shrine_options").withStyle(Style.EMPTY.withUnderlined(true)).withStyle(ChatFormatting.BLACK);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new AoEShrineMenu(id, inventory, this);
    }
}
