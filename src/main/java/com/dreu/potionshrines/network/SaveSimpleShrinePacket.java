package com.dreu.potionshrines.network;

import com.dreu.potionshrines.screen.simple.SimpleShrineMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class SaveSimpleShrinePacket {
    private final String effect, icon;
    private final int amplifier, duration, maxCooldown;
    private final boolean replenish;
    public SaveSimpleShrinePacket(String effect, int amplifier, int duration, int maxCooldown, boolean replenish, String icon){
        this.effect = effect;
        this.amplifier = amplifier;
        this.duration = duration;
        this.maxCooldown = maxCooldown;
        this.replenish = replenish;
        this.icon = icon;
    }
    public SaveSimpleShrinePacket(FriendlyByteBuf buffer) {
        effect = buffer.readCharSequence(buffer.readInt(), StandardCharsets.UTF_8).toString();
        amplifier = buffer.readInt();
        duration = buffer.readInt();
        maxCooldown = buffer.readInt();
        replenish = buffer.readBoolean();
        icon = buffer.readCharSequence(buffer.readInt(), StandardCharsets.UTF_8).toString();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(effect.length());
        buffer.writeCharSequence(effect, StandardCharsets.UTF_8);
        buffer.writeInt(amplifier);
        buffer.writeInt(duration);
        buffer.writeInt(maxCooldown);
        buffer.writeBoolean(replenish);
        buffer.writeInt(icon.length());
        buffer.writeCharSequence(icon, StandardCharsets.UTF_8);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.containerMenu instanceof SimpleShrineMenu menu) {
                menu.shrineEntity.setEffect(effect);
                menu.shrineEntity.setAmplifier(amplifier);
                menu.shrineEntity.setDuration(duration);
                menu.shrineEntity.setMaxCooldown(maxCooldown);
                menu.shrineEntity.setCanReplenish(replenish);
                menu.shrineEntity.setIcon(icon);
                // Sync with the client if necessary
                menu.shrineEntity.setChanged();
            }
        });
        context.setPacketHandled(true);
    }
}
