package me.yeoc.anticrash;

import me.yeoc.anticrash.module.anticrash.AntiCrashManager;
import me.yeoc.anticrash.module.antispam.SpamEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = AntiCrash.MODID, version = AntiCrash.VERSION)
public class AntiCrash
{
    public static final String MODID = "AntiCrash";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        //Minecraft.getMinecraft().getNetHandler().getNetworkManager();
        //System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
        //ClientCommandHandler.instance.registerCommand(new ServerCrasherCommand());
        MinecraftForge.EVENT_BUS.register(new SpamEventHandler());
        new AntiCrashManager();
    }
}
