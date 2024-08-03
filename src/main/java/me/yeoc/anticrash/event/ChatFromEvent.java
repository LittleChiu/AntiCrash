package me.yeoc.anticrash.event;

import lombok.Getter;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ChatFromEvent extends Event {


    @Getter
    IChatComponent iChatComponent;
    public ChatFromEvent(IChatComponent iChatComponent){
        this.iChatComponent = iChatComponent;
    }
}
