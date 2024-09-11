package com.dreu.potionshrines.network;

import com.dreu.potionshrines.screen.ShrineMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ResetCooldownPacket {
    public ResetCooldownPacket() {}
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.containerMenu instanceof ShrineMenu menu) {
                menu.resetCooldown();
            }
        });
        context.setPacketHandled(true);
    }
}
