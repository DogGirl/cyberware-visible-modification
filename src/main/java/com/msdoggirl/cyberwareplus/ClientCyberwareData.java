
package com.msdoggirl.cyberwareplus;

import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class ClientCyberwareData {
    private static final Map<UUID, CyberwareSkinSwapper.PlayerVisualState> states = new HashMap<>();

    public static void setState(UUID uuid, CyberwareSkinSwapper.PlayerVisualState state) {
        states.put(uuid, copyState(state));
    }

    public static CyberwareSkinSwapper.PlayerVisualState getState(UUID uuid) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.getUUID().equals(uuid)) {
            CyberwareSkinSwapper.PlayerVisualState state = new CyberwareSkinSwapper.PlayerVisualState();
            mc.player.getCapability(CyberwareCapabilityProvider.CYBERWARE_CAPABILITY).ifPresent(data -> {
                state.syntheticSkin = data.isCyberwareInstalled(ModItems.SYNTHETIC_SKIN.get());
                state.cyberEye = data.isCyberwareInstalled(ModItems.CYBER_EYE.get());
                state.cyberHeart = data.isCyberwareInstalled(ModItems.CARDIOMECHANIC_PUMP.get());
                state.humanRightArm = data.isCyberwareInstalled(ModItems.HUMAN_RIGHT_ARM.get());
                state.humanLeftArm = data.isCyberwareInstalled(ModItems.HUMAN_LEFT_ARM.get());
                state.humanRightLeg = data.isCyberwareInstalled(ModItems.HUMAN_RIGHT_LEG.get());
                state.humanLeftLeg = data.isCyberwareInstalled(ModItems.HUMAN_LEFT_LEG.get());
                state.cyberRightArm = data.isCyberwareInstalled(ModItems.CYBER_ARM_RIGHT.get());
                state.cyberLeftArm = data.isCyberwareInstalled(ModItems.CYBER_ARM_LEFT.get());
                state.cyberRightLeg = data.isCyberwareInstalled(ModItems.CYBER_LEG_RIGHT.get());
                state.cyberLeftLeg = data.isCyberwareInstalled(ModItems.CYBER_LEG_LEFT.get());
            });
            return state;
        } else {
            return states.getOrDefault(uuid, new CyberwareSkinSwapper.PlayerVisualState());
        }
    }

    public static boolean hasSyntheticSkin(UUID uuid) {
        return getState(uuid).syntheticSkin;
    }

    public static boolean hasCyberEye(UUID uuid) {
        return getState(uuid).cyberEye;
    }

    public static boolean hasCyberHeart(UUID uuid) {
        return getState(uuid).cyberHeart;
    }

    public static boolean hasHumanRightArm(UUID uuid) {
        return getState(uuid).humanRightArm;
    }

    public static boolean hasHumanLeftArm(UUID uuid) {
        return getState(uuid).humanLeftArm;
    }

    public static boolean hasHumanRightLeg(UUID uuid) {
        return getState(uuid).humanRightLeg;
    }

    public static boolean hasHumanLeftLeg(UUID uuid) {
        return getState(uuid).humanLeftLeg;
    }

    public static boolean hasCyberRightArm(UUID uuid) {
        return getState(uuid).cyberRightArm;
    }

    public static boolean hasCyberLeftArm(UUID uuid) {
        return getState(uuid).cyberLeftArm;
    }

    public static boolean hasCyberRightLeg(UUID uuid) {
        return getState(uuid).cyberRightLeg;
    }

    public static boolean hasCyberLeftLeg(UUID uuid) {
        return getState(uuid).cyberLeftLeg;
    }

    public static void clear() {
        states.clear();
    }

    private static CyberwareSkinSwapper.PlayerVisualState copyState(CyberwareSkinSwapper.PlayerVisualState original) {
        CyberwareSkinSwapper.PlayerVisualState copy = new CyberwareSkinSwapper.PlayerVisualState();
        copy.syntheticSkin = original.syntheticSkin;
        copy.cyberEye = original.cyberEye;
        copy.cyberHeart = original.cyberHeart;
        copy.humanRightArm = original.humanRightArm;
        copy.humanLeftArm = original.humanLeftArm;
        copy.humanRightLeg = original.humanRightLeg;
        copy.humanLeftLeg = original.humanLeftLeg;
        copy.cyberRightArm = original.cyberRightArm;
        copy.cyberLeftArm = original.cyberLeftArm;
        copy.cyberRightLeg = original.cyberRightLeg;
        copy.cyberLeftLeg = original.cyberLeftLeg;
        return copy;
    }
}