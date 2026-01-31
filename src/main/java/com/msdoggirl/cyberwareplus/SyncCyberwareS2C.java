
package com.msdoggirl.cyberwareplus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncCyberwareS2C {
    public final UUID playerId;
    public final boolean syntheticSkin;
    public final boolean cyberEye;
    public final boolean cyberHeart;
    public final boolean humanRightArm;
    public final boolean humanLeftArm;
    public final boolean humanRightLeg;
    public final boolean humanLeftLeg;
    public final boolean cyberRightArm;
    public final boolean cyberLeftArm;
    public final boolean cyberRightLeg;
    public final boolean cyberLeftLeg;

    public SyncCyberwareS2C(UUID playerId, boolean syntheticSkin, boolean cyberEye, boolean cyberHeart,
                            boolean humanRightArm, boolean humanLeftArm, boolean humanRightLeg, boolean humanLeftLeg,
                            boolean cyberRightArm, boolean cyberLeftArm, boolean cyberRightLeg, boolean cyberLeftLeg) {
        this.playerId = playerId;
        this.syntheticSkin = syntheticSkin;
        this.cyberEye = cyberEye;
        this.cyberHeart = cyberHeart;
        this.humanRightArm = humanRightArm;
        this.humanLeftArm = humanLeftArm;
        this.humanRightLeg = humanRightLeg;
        this.humanLeftLeg = humanLeftLeg;
        this.cyberRightArm = cyberRightArm;
        this.cyberLeftArm = cyberLeftArm;
        this.cyberRightLeg = cyberRightLeg;
        this.cyberLeftLeg = cyberLeftLeg;
    }

    public static void encode(SyncCyberwareS2C msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerId);
        buf.writeBoolean(msg.syntheticSkin);
        buf.writeBoolean(msg.cyberEye);
        buf.writeBoolean(msg.cyberHeart);
        buf.writeBoolean(msg.humanRightArm);
        buf.writeBoolean(msg.humanLeftArm);
        buf.writeBoolean(msg.humanRightLeg);
        buf.writeBoolean(msg.humanLeftLeg);
        buf.writeBoolean(msg.cyberRightArm);
        buf.writeBoolean(msg.cyberLeftArm);
        buf.writeBoolean(msg.cyberRightLeg);
        buf.writeBoolean(msg.cyberLeftLeg);
    }

    public static SyncCyberwareS2C decode(FriendlyByteBuf buf) {
        return new SyncCyberwareS2C(
                buf.readUUID(),
                buf.readBoolean(), buf.readBoolean(), buf.readBoolean(),
                buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(),
                buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean()
        );
    }

    public static void handle(SyncCyberwareS2C msg, Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            CyberwareSkinSwapper.PlayerVisualState state = new CyberwareSkinSwapper.PlayerVisualState();
            state.syntheticSkin = msg.syntheticSkin;
            state.cyberEye = msg.cyberEye;
            state.cyberHeart = msg.cyberHeart;
            state.humanRightArm = msg.humanRightArm;
            state.humanLeftArm = msg.humanLeftArm;
            state.humanRightLeg = msg.humanRightLeg;
            state.humanLeftLeg = msg.humanLeftLeg;
            state.cyberRightArm = msg.cyberRightArm;
            state.cyberLeftArm = msg.cyberLeftArm;
            state.cyberRightLeg = msg.cyberRightLeg;
            state.cyberLeftLeg = msg.cyberLeftLeg;
            ClientCyberwareData.setState(msg.playerId, state);
        });
        ctx.setPacketHandled(true);
    }
}