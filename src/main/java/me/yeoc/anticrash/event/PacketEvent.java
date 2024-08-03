package me.yeoc.anticrash.event;

import lombok.Getter;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PacketEvent extends Event {
    @Getter
    Packet<?> packet;

    public PacketEvent(Packet<?> packet){
        this.packet = packet;
    }
}
