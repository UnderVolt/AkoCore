package io.undervolt.cubecore;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.logging.log4j.Logger;
import io.undervolt.cubecore.net.CustomPayloadPacket;
import io.undervolt.cubecore.net.NetHandler;
import io.undervolt.cubecore.proxy.CommonProxy;
import io.undervolt.cubecore.registry.CubecoreRegistry;

@Mod(modid = CubecoreSettings.MODID, name = CubecoreSettings.NAME, version = CubecoreSettings.VERSION)
public class CubecoreMod {

    @Mod.Instance
    public static CubecoreMod INSTANCE = new CubecoreMod();

    @SidedProxy(clientSide = CubecoreSettings.CLIENT_PROXY_CLASS, serverSide = CubecoreSettings.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    public static NetHandler network = new NetHandler();
    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        network.init();
        proxy.preinit();
        CubecoreMod.logger = event.getModLog();
        CubecoreRegistry.init();
        CubecoreRegistry.registerEntries();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }
}