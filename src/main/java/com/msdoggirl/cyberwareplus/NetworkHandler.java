
package com.msdoggirl.cyberwareplus;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("cyberwareplus", "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(id++, SyncCyberwareC2S.class, SyncCyberwareC2S::encode, SyncCyberwareC2S::decode, SyncCyberwareC2S::handle);
        INSTANCE.registerMessage(id++, SyncCyberwareS2C.class, SyncCyberwareS2C::encode, SyncCyberwareS2C::decode, SyncCyberwareS2C::handle);
    }
}