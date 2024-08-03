package me.yeoc.anticrash.injection.mixins;

import me.yeoc.anticrash.event.ChatFromEvent;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiNewChat.class)
public class MixinChatMessage {

    @Inject(method = "printChatMessageWithOptionalDeletion",at = @At("HEAD"))
    public void printChatMessageWithOptionalDeletion(IChatComponent chat
            , int i, CallbackInfo ci) {
        if (chat.getUnformattedText().contains("§cAntiCrash §7» §f")){
            MinecraftForge.EVENT_BUS.post(new ChatFromEvent(chat));
        }

    }

}
