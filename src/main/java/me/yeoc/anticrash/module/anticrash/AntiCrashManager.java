package me.yeoc.anticrash.module.anticrash;

import me.yeoc.anticrash.module.anticrash.exploits.*;
import net.minecraftforge.common.MinecraftForge;

import java.util.Arrays;
import java.util.List;

public class AntiCrashManager {

    public AntiCrashManager(){
        init();
    }

    void init(){
        List<IExploit> exploits = Arrays.asList(new Explosion(),new SpawnObject(),new BlockChange(),new EntityStatus()
        ,new SoundEffect(),new ChangeGameState(),new Camera());
        MinecraftForge.EVENT_BUS.register(new AntiCrashListener(exploits));
    }
}
