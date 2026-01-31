package com.msdoggirl.cyberwareplus.mixin;

import com.msdoggirl.cyberwareplus.SkinSwapper;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {

    @Inject(
        method = "getSkinTextureLocation()Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void overrideSkinTextureLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;

        // Ask SkinSwapper for this specific player's composited skin (if any)
        ResourceLocation swapped = SkinSwapper.getSwappedSkinForPlayer(player);
        if (swapped != null) {
            cir.setReturnValue(swapped);
        }
        // Otherwise falls back to vanilla Mojang skin
    }
}