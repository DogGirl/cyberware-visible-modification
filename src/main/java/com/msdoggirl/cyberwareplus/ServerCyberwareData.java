
package com.msdoggirl.cyberwareplus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerCyberwareData {
    private static final ServerCyberwareData INSTANCE = new ServerCyberwareData();

    public static ServerCyberwareData get() {
        return INSTANCE;
    }

    private final Map<UUID, CyberwareSkinSwapper.PlayerVisualState> states = new HashMap<>();

    public void setState(UUID uuid, CyberwareSkinSwapper.PlayerVisualState state) {
        states.put(uuid, copyState(state));
    }

    public CyberwareSkinSwapper.PlayerVisualState getState(UUID uuid) {
        return states.get(uuid);
    }

    public void removeState(UUID uuid) {
        states.remove(uuid);
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