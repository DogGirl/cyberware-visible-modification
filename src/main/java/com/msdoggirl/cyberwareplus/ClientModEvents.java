package com.msdoggirl.cyberwareplus;

import com.msdoggirl.cyberwareplus.CyberGlowLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "cyberwareplus", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientModEvents {

    private static boolean glowLayerAdded = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (glowLayerAdded) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        var dispatcher = mc.getEntityRenderDispatcher();
        if (dispatcher == null) return;

        boolean added = false;

        PlayerRenderer defaultRenderer = (PlayerRenderer) dispatcher.getSkinMap().get("default");
        if (defaultRenderer != null) {
            defaultRenderer.addLayer(new CyberGlowLayer(defaultRenderer));
            ////System.out.println("[DEBUG] Glow layer added to default renderer");
            added = true;
        }

        PlayerRenderer slimRenderer = (PlayerRenderer) dispatcher.getSkinMap().get("slim");
        if (slimRenderer != null) {
            slimRenderer.addLayer(new CyberGlowLayer(slimRenderer));
            ////System.out.println("[DEBUG] Glow layer added to slim renderer");
            added = true;
        }

        if (added) {
            glowLayerAdded = true;
            ////System.out.println("[Cyberware+] Glow layer fully added");
        }
    }
}