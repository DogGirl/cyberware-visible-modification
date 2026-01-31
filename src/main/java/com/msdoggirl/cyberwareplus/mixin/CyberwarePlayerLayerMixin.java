package com.msdoggirl.cyberwareplus.mixin;

import com.maxwell.cyber_ware_port.client.upgrades.CyberwarePlayerLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CyberwarePlayerLayer.class)
public abstract class CyberwarePlayerLayerMixin {

    @Inject(
        method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V",
        at = @At("HEAD"),
        cancellable = true,
        remap = false  // Since targeting another mod's class, disable remapping if needed
    )
    private void cancelRender(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                              AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                              float partialTick, float ageInTicks, float netHeadYaw, float headPitch,
                              CallbackInfo ci) {
        // Always cancel to let skin swapping handle visuals
        ci.cancel();
    }
}