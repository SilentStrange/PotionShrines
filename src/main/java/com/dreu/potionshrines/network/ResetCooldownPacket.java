package com.dreu.potionshrines.network;

import com.dreu.potionshrines.screen.AoEShrineMenu;
import com.dreu.potionshrines.screen.aoe.AoEShrineMenu;
import com.dreu.potionshrines.screen.simple.SimpleShrineMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ResetCooldownPacket {

    public ResetCooldownPacket() {}

    public ResetCooldownPacket(FriendlyByteBuf buffer) {}

    public void toBytes(FriendlyByteBuf buffer) {}

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.containerMenu instanceof AoEShrineMenu menu) {
                menu.shrineEntity.setRemainingCooldown(0);
                // Sync with the client if necessary
                menu.shrineEntity.setChanged();
            }
        });
        context.setPacketHandled(true);
    }
}
