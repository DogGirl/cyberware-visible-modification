
package com.msdoggirl.cyberwareplus;

import java.util.UUID;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = "cyberwareplus", bus = Mod.EventBusSubscriber.Bus.FORGE, value = {Dist.CLIENT, Dist.DEDICATED_SERVER})
public class ServerEvents {

    static {
        System.out.println("[Cyberware+] ServerEvents class static block executed - registration should be active");
    }
    
    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerCyberwareData.get().removeState(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getTarget() instanceof ServerPlayer target)) return;
        CyberwareSkinSwapper.PlayerVisualState state = ServerCyberwareData.get().getState(target.getUUID());
        if (state != null) {
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                    new SyncCyberwareS2C(target.getUUID(), state.syntheticSkin, state.cyberEye, state.cyberHeart,
                            state.humanRightArm, state.humanLeftArm, state.humanRightLeg, state.humanLeftLeg,
                            state.cyberRightArm, state.cyberLeftArm, state.cyberRightLeg, state.cyberLeftLeg));
        }
    }

    @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            System.out.println("New player joined");
            if (!(event.getEntity() instanceof ServerPlayer newPlayer)) {
                return;
            }
      
            
            ServerCyberwareData data = ServerCyberwareData.get();

            for (ServerPlayer online : event.getEntity().getServer().getPlayerList().getPlayers()) {
                if (online == newPlayer) continue; 
                UUID uuid = online.getUUID();
                CyberwareSkinSwapper.PlayerVisualState state = data.getState(uuid);

                System.out.println("[Cyberware+] Sending sync for " + online.getScoreboardName() + " to new player " + newPlayer.getScoreboardName());

                if (state != null) {
                    NetworkHandler.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> newPlayer),
                        new SyncCyberwareS2C(
                            uuid,
                            state.syntheticSkin, state.cyberEye, state.cyberHeart,
                            state.humanRightArm, state.humanLeftArm, state.humanRightLeg, state.humanLeftLeg,
                            state.cyberRightArm, state.cyberLeftArm, state.cyberRightLeg, state.cyberLeftLeg
                        )
                    );
                }
            }


            CyberwareSkinSwapper.PlayerVisualState ownState = data.getState(newPlayer.getUUID());
            if (ownState != null) {
                NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> newPlayer),
                    new SyncCyberwareS2C(
                        newPlayer.getUUID(),
                        ownState.syntheticSkin, ownState.cyberEye, ownState.cyberHeart,
                        ownState.humanRightArm, ownState.humanLeftArm, ownState.humanRightLeg, ownState.humanLeftLeg,
                        ownState.cyberRightArm, ownState.cyberLeftArm, ownState.cyberRightLeg, ownState.cyberLeftLeg
                    )
                );
            }
        }
}