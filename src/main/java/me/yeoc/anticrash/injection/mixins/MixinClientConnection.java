package me.yeoc.anticrash.injection.mixins;

import io.netty.channel.ChannelHandlerContext;
import me.yeoc.anticrash.event.PacketEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinClientConnection {
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo callbackInfo)
    {
        //System.out.println("Packet Sent: " + packet.toString());
        PacketEvent event = new PacketEvent(packet);

        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled() && callbackInfo.isCancellable())
        {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void onChannelRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo)
    {
        //System.out.println("Packet Recieved: " + packet.toString());
        PacketEvent event = new PacketEvent(packet);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled() && callbackInfo.isCancellable())
        {
            callbackInfo.cancel();
        }
    }
}
