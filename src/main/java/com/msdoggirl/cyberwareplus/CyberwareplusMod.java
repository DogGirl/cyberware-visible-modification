
package com.msdoggirl.cyberwareplus;

import com.msdoggirl.cyberwareplus.ClientModEvents;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod("cyberwareplus")
public class CyberwareplusMod {

    public CyberwareplusMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(ServerEvents::onPlayerLogout);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(ServerEvents::onStartTracking);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(ServerEvents::onPlayerLoggedIn);


        if (FMLLoader.getDist() == Dist.CLIENT) {

            net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(CyberwareSkinSwapper::onClientTick);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onClientTick);
            NetworkHandler.init();
        
        }
    }
    
}