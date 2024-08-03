package me.yeoc.anticrash.module.anticrash;


import me.yeoc.anticrash.event.PacketEvent;
import me.yeoc.anticrash.util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.*;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class AntiCrashListener {
    static Minecraft mc = Minecraft.getMinecraft();

    List<IExploit> exploits;
    TimerUtil timer = new TimerUtil();
    public AntiCrashListener(List<IExploit> iExploits){
        exploits = iExploits;
    }
    @SubscribeEvent
    public void onPacketReceiving(PacketEvent e) {
        Packet<?> packet = e.getPacket();
        //只监听收到的包
        //排除心跳包
        if (packet instanceof S03PacketTimeUpdate || packet instanceof S00PacketKeepAlive
        || packet instanceof C03PacketPlayer || packet instanceof S14PacketEntity
        || packet instanceof S18PacketEntityTeleport || packet instanceof S19PacketEntityHeadLook
        || packet instanceof C00PacketKeepAlive || packet instanceof S12PacketEntityVelocity
        || packet instanceof S0BPacketAnimation) return;
//        if (mc.getNetHandler().getNetworkManager().isChannelOpen()){
//            mc.thePlayer.addChatMessage(new ChatComponentText(packet.toString()));
//        }
        for (IExploit exploit : exploits) {
            boolean b = exploit.handlePacket(packet);
            if (!b){
                e.setCanceled(true);
                if (timer.sleep(10)){
                    sendMessage("成功截获崩端数据包! ("+packet.getClass().getSimpleName()+")");

                }
                break;
            }
        }
        //System.out.println(packet.toString());
    }
    static String prefix = "§cAntiCrash §7» §f";
    public static void sendMessage(String s){
        mc.thePlayer.addChatMessage(new ChatComponentText(prefix+s));
    }


}
