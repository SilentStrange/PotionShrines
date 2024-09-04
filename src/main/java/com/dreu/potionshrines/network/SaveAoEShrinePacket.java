package com.dreu.potionshrines.network;

import com.dreu.potionshrines.screen.AoEShrineMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class SaveAoEShrinePacket {
    private final String effect, icon;
    private final int amplifier, duration, maxCooldown, radius;
    private final boolean effectPlayers, effectMonsters, replenish;
    public SaveAoEShrinePacket(String effect, int amplifier, int duration, int maxCooldown, int radius, boolean effectPlayers, boolean effectMonsters, boolean replenish, String icon){
        this.effect = effect;
        this.amplifier = amplifier;
        this.duration = duration;
        this.maxCooldown = maxCooldown;
        this.radius = radius;
        this.effectPlayers = effectPlayers;
        this.effectMonsters = effectMonsters;
        this.replenish = replenish;
        this.icon = icon;
    }
    public SaveAoEShrinePacket(FriendlyByteBuf buffer) {
        effect = buffer.readCharSequence(buffer.readInt(), StandardCharsets.UTF_8).toString();
        amplifier = buffer.readInt();
        duration = buffer.readInt();
        maxCooldown = buffer.readInt();
        radius = buffer.readInt();
        effectPlayers = buffer.readBoolean();
        effectMonsters = buffer.readBoolean();
        replenish = buffer.readBoolean();
        icon = buffer.readCharSequence(buffer.readInt(), StandardCharsets.UTF_8).toString();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(effect.length());
        buffer.writeCharSequence(effect, StandardCharsets.UTF_8);
        buffer.writeInt(amplifier);
        buffer.writeInt(duration);
        buffer.writeInt(maxCooldown);
        buffer.writeInt(radius);
        buffer.writeBoolean(effectPlayers);
        buffer.writeBoolean(effectMonsters);
        buffer.writeBoolean(replenish);
        buffer.writeInt(icon.length());
        buffer.writeCharSequence(icon, StandardCharsets.UTF_8);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.containerMenu instanceof AoEShrineMenu menu) {
                menu.shrineEntity.setEffect(effect);
                menu.shrineEntity.setAmplifier(amplifier);
                menu.shrineEntity.setDuration(duration);
                menu.shrineEntity.setMaxCooldown(maxCooldown);
                menu.shrineEntity.setRadius(radius);
                menu.shrineEntity.setCanEffectPlayers(effectPlayers);
                menu.shrineEntity.setCanEffectMonsters(effectMonsters);
                menu.shrineEntity.setCanReplenish(replenish);
                menu.shrineEntity.setIcon(icon);
                // Sync with the client if necessary
                menu.shrineEntity.setChanged();
            }
        });
        context.setPacketHandled(true);
    }
}
