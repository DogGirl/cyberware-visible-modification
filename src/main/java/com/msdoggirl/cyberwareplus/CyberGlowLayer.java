package com.msdoggirl.cyberwareplus;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import java.lang.reflect.Method;

import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.init.ModItems;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.Animator;

@OnlyIn(Dist.CLIENT)
public class CyberGlowLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    // Glow texture - black background + bright/white where glow should be
    private static final ResourceLocation GLOW_TEXTURE = new ResourceLocation("cyberwareplus", "textures/skins/cyber_glow.png");

    public CyberGlowLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer != null ? renderer : null); // allow null parent in mixin case
    }



    public boolean isEpicFightModelActive(AbstractClientPlayer player) {
        if (!ModList.get().isLoaded("epicfight")) {
            return false;
        }

        try {
            Class<?> capsClass = Class.forName("yesman.epicfight.world.capabilities.EpicFightCapabilities");
            Method getPatchMethod = capsClass.getMethod("getEntityPatch",
                    net.minecraft.world.entity.Entity.class, Class.class);

            Object patchObj = getPatchMethod.invoke(null, player,
                    Class.forName("yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch"));

            if (patchObj == null) {
                //System.out.println("EpicFight: No PlayerPatch attached");
                return false;
            }

            //System.out.println("EpicFight patch attached - querying state...");

            // Variant 1: Try getLivingMotion() - common in many versions
            try {
                Method getLivingMotion = patchObj.getClass().getMethod("getLivingMotion");
                Enum<?> motion = (Enum<?>) getLivingMotion.invoke(patchObj);
                String motionName = motion.name();
                //System.out.println("EpicFight current living motion: " + motionName);
                // Customize: true when in combat-related motion
                return motionName.contains("BATTLE") || motionName.contains("COMBAT") ||
                    motionName.contains("BLOCK") || motionName.contains("ATTACK") ||
                    motionName.contains("KATANA") || !motionName.equals("IDLE") && !motionName.equals("WALK");
            } catch (NoSuchMethodException ignored) { }

            // Variant 2: ClientEngine.isBattleMode() (some crashes point here for client-side)
            try {
                Class<?> engineClass = Class.forName("yesman.epicfight.client.ClientEngine");
                Method getInstance = engineClass.getMethod("getInstance");
                Object engine = getInstance.invoke(null);
                if (engine != null) {
                    Method isBattle = engine.getClass().getMethod("isBattleMode");
                    boolean inBattle = (boolean) isBattle.invoke(engine);
                    //System.out.println("EpicFight ClientEngine.isBattleMode(): " + inBattle);
                    return inBattle;
                }
            } catch (Exception ignored) { }

            // Variant 3: Animator check (fallback - if any animation is playing beyond vanilla)
            try {
                Method getAnimator = patchObj.getClass().getMethod("getAnimator");  // or getClientAnimator()
                Object animator = getAnimator.invoke(patchObj);
                if (animator != null) {
                    Method isOnGoing = animator.getClass().getMethod("isOnGoing");
                    boolean animActive = (boolean) isOnGoing.invoke(animator);
                    //System.out.println("EpicFight animator isOnGoing: " + animActive);
                    return animActive;  // rough but works when custom anims run
                }
            } catch (Exception ignored) { }

            //System.out.println("EpicFight: No recognized mode/motion method found");
            return false;

        } catch (Exception e) {
            e.printStackTrace();  // Now print always for debug
            return false;
        }
    }



    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                       AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    // Epic Fight compatibility: disable glow when Epic Fight is overriding the model with custom animations

        if(isEpicFightModelActive(player)) {
            //System.out.println("Epic fight model active.");
            return;
        }
         ////System.out.println("[CyberGlowLayer] render() called for player: " + player.getName().getString());

        player.getCapability(CyberwareCapabilityProvider.CYBERWARE_CAPABILITY).ifPresent(cap -> {
            ////System.out.println("[CyberGlowLayer] Capability present. Checking for cyberware...");

            if (cap.isCyberwareInstalled(ModItems.SYNTHETIC_SKIN.get())) {
                ////System.out.println("[CyberGlowLayer] Synthetic skin installed - skipping glow");
                return;
            }

            PlayerModel<AbstractClientPlayer> parentModel = getParentModel();

            // Use RenderType.eyes for true glow (same as Glowing Eyes mod)
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(GLOW_TEXTURE));

            // Head glow
            if (cap.isCyberwareInstalled(ModItems.CYBER_EYE.get())) {
                ////System.out.println("[CyberGlowLayer] Rendering glowing head parts");
                poseStack.pushPose();
                parentModel.head.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
                poseStack.popPose();
            }

            // Body glow
            if (cap.isCyberwareInstalled(ModItems.CARDIOMECHANIC_PUMP.get())) {
                ////System.out.println("[CyberGlowLayer] Rendering glowing body");
                parentModel.body.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            }

            // Right arm glow
            if (cap.isCyberwareInstalled(ModItems.CYBER_ARM_RIGHT.get())) {
                ////System.out.println("[CyberGlowLayer] Rendering glowing right arm");
                parentModel.rightArm.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            }

            // Left arm glow
            if (cap.isCyberwareInstalled(ModItems.CYBER_ARM_LEFT.get())) {
                ////System.out.println("[CyberGlowLayer] Rendering glowing left arm");
                parentModel.leftArm.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            }

            // Right leg glow
            if (cap.isCyberwareInstalled(ModItems.CYBER_LEG_RIGHT.get())) {
                ////System.out.println("[CyberGlowLayer] Rendering glowing right leg");
                parentModel.rightLeg.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            }

            // Left leg glow
            if (cap.isCyberwareInstalled(ModItems.CYBER_LEG_LEFT.get())) {
                ////System.out.println("[CyberGlowLayer] Rendering glowing left leg");
                parentModel.leftLeg.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            }
        });
    }
}