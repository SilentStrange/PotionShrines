package com.dreu.potionshrines.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static com.dreu.potionshrines.PotionShrines.MODID;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        CHANNEL.registerMessage(0, ResetCooldownPacket.class, ((r, b) -> {}), byteBuf -> new ResetCooldownPacket(), ResetCooldownPacket::handle);
        CHANNEL.registerMessage(1, SaveAoEShrinePacket.class, SaveAoEShrinePacket::toBytes, SaveAoEShrinePacket::new, SaveAoEShrinePacket::handle);
        CHANNEL.registerMessage(2, SaveSimpleShrinePacket.class, SaveSimpleShrinePacket::toBytes, SaveSimpleShrinePacket::new, SaveSimpleShrinePacket::handle);
        CHANNEL.registerMessage(2, SaveAuraShrinePacket.class, SaveAuraShrinePacket::toBytes, SaveAuraShrinePacket::new, SaveAuraShrinePacket::handle);
    }
}
