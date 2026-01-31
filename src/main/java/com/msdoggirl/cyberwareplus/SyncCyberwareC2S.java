
package com.msdoggirl.cyberwareplus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class SyncCyberwareC2S {
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

    public SyncCyberwareC2S(boolean syntheticSkin, boolean cyberEye, boolean cyberHeart,
                            boolean humanRightArm, boolean humanLeftArm, boolean humanRightLeg, boolean humanLeftLeg,
                            boolean cyberRightArm, boolean cyberLeftArm, boolean cyberRightLeg, boolean cyberLeftLeg) {
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

    public static void encode(SyncCyberwareC2S msg, FriendlyByteBuf buf) {
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

    public static SyncCyberwareC2S decode(FriendlyByteBuf buf) {
        return new SyncCyberwareC2S(
                buf.readBoolean(), buf.readBoolean(), buf.readBoolean(),
                buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(),
                buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean()
        );
    }

    public static void handle(SyncCyberwareC2S msg, Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            if (ctx.getSender() == null) return;
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
            ServerCyberwareData.get().setState(ctx.getSender().getUUID(), state);
            NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> ctx.getSender()),
                    new SyncCyberwareS2C(ctx.getSender().getUUID(), state.syntheticSkin, state.cyberEye, state.cyberHeart,
                            state.humanRightArm, state.humanLeftArm, state.humanRightLeg, state.humanLeftLeg,
                            state.cyberRightArm, state.cyberLeftArm, state.cyberRightLeg, state.cyberLeftLeg));
        });
        ctx.setPacketHandled(true);
    }
}