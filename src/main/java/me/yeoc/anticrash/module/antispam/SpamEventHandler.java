package me.yeoc.anticrash.module.antispam;

import me.yeoc.anticrash.event.ChatFromEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class SpamEventHandler {
	static Minecraft mc = Minecraft.getMinecraft();
	private final ArrayList<SpamCount> messages = new ArrayList<>();
	private final ArrayList<String> ignored = new ArrayList<>();

	@SubscribeEvent
	public void onChat(ChatFromEvent event) {
		IChatComponent mes = event.getIChatComponent();
		for (String ig : ignored) {
			if (mes.getUnformattedText().contains(ig)) {
				ChatComponentText igMes = new ChatComponentText("Ignored");
				igMes.getChatStyle().setColor(EnumChatFormatting.RED);
				igMes.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, mes));
				Minecraft.getMinecraft().thePlayer.addChatMessage(igMes);
				mes = igMes;
				//event.setCanceled(true);
			}
		}

		/*
		 * int mesLength = mes.getUnformattedText().length(), caps = 0;
		 * 
		 * for(char c : mes.getUnformattedText().toCharArray()){
		 * if(Character.isUpperCase(c)){ caps++; } }
		 */

		boolean existsAlready = false;

		for (SpamCount spam : messages) {
			if (spam.isSame(mes)) {
				existsAlready = true;
				long currentTime = System.currentTimeMillis();
				if ((currentTime - spam.getTime()) < 30000) {// 30 seconds
					spam.increaseCounter();
					mes.appendText(EnumChatFormatting.GOLD + " [" + EnumChatFormatting.GRAY + "x" + EnumChatFormatting.RED
							+ spam.getCounter() + EnumChatFormatting.GOLD + "]");
				} else {
					spam.resetCounter();
				}
				spam.setTime(currentTime);
			}
		}

		if (!existsAlready) {
			messages.add(new SpamCount(mes));
		} else {
			List<ChatLine> chatLines = getChatLines();

			if (chatLines != null) {
				for (int i = 0; i < chatLines.size(); i++) {
					ChatLine line = chatLines.get(i);
					String counterStr = EnumChatFormatting.GOLD + " [" + EnumChatFormatting.GRAY + "x";

					String lineStr = line.getChatComponent().getUnformattedText();

					int lastIndex = lineStr.lastIndexOf(counterStr);
					if (lastIndex > 0) {
						lineStr = lineStr.substring(0, lastIndex);
					}

					String mesStr = mes.getUnformattedText();

					lastIndex = mesStr.lastIndexOf(counterStr);
					if (lastIndex > 0) {
						mesStr = mesStr.substring(0, lastIndex);
					}

					if (lineStr.equals(mesStr)) {
						chatLines.remove(line);
					}
				}
			}

		}
		mc.ingameGUI.getChatGUI().refreshChat();

	}

	@SubscribeEvent
	public void onDisconnect(ClientDisconnectionFromServerEvent event) {
		messages.clear();
	}
//
//	@SubscribeEvent
//	public static void onSendChat(ClientChatReceivedEvent event) {
//		String mes = event.getMessage();
//		if (mes.startsWith(".clearspam")) {
//			event.setCanceled(true);
//			ClearSpam.getChatGui().addToSentMessages(mes);
//			if (mes.equals(".clearspam toggle")) {
//				isDisabled = !isDisabled;
//				sendMessage(("ClearSpam has been " + (isDisabled ? "disabled" : "enabled" + ".")));
//			} else if (mes.equals(".clearspam on")) {
//				isDisabled = false;
//				sendMessage(("ClearSpam has been enabled."));
//			} else if (mes.equals(".clearspam off")) {
//				isDisabled = true;
//				sendMessage(("ClearSpam has been disabled."));
//			} else if (mes.equals(".clearspam reset")) {
//				messages.clear();
//				sendMessage(("ClearSpam counter has been reset."));
//			} else if (mes.startsWith(".clearspam block")) {
//				String ignoreStr = mes.substring(17);
//				ignored.add(ignoreStr);
//				sendMessage(("Messages containing \"" + ignoreStr + "\" will no longer reach chat."));
//			} else if (mes.equals(".clearspam") || mes.equals(".clearspam help")) {
//				sendCommandList();
//			} else {
//				sendMessage(EnumChatFormatting.RED + "That ClearSpam command was not recognized.");
//				sendMessage("Type \".clearspam help\" for a list of commands.");
//			}
//		}
//	}

//	public static void sendCommandList() {
//		sendMessage("-=-=-=-=-= ClearSpam Help =-=-=-=-=-", EnumChatFormatting.DARK_GREEN);
//		sendMessage(".clearspam toggle" + EnumChatFormatting.GRAY + " - Toggles ClearSpam");
//		sendMessage(".clearspam on/off" + EnumChatFormatting.GRAY + " - Turns ClearSpam on or off");
//		sendMessage(".clearspam reset" + EnumChatFormatting.GRAY + " - Resets the ClearSpam counters");
//		sendMessage(".clearspam block <string>" + EnumChatFormatting.GRAY + " - Block messages that contain the string");
//	}

	public static void sendMessage(String message) {
		sendMessage(message, EnumChatFormatting.GOLD);
	}
	@SuppressWarnings("unchecked")
	@Nullable
	public static List<ChatLine> getChatLines() {
		List<ChatLine> chatLine = null;

		try {
			Field chatLinesField = ReflectionHelper.findField(GuiNewChat.class,
					"chatLines", "field_146252_h");
			// Useful source for getting SRG names: mcpbot.bspk.rs
			// field_146252_h,chatLines,0,Chat lines to be displayed in the chat
			// box
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(chatLinesField, chatLinesField.getModifiers() & ~Modifier.FINAL);

			chatLine = (List<ChatLine>) chatLinesField.get(mc.ingameGUI.getChatGUI());

		} catch (IllegalArgumentException | ReflectionHelper.UnableToFindFieldException | NoSuchFieldException | SecurityException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return chatLine;
	}
	public static void sendMessage(String message, EnumChatFormatting color) {
		ChatComponentText mes = new ChatComponentText(message);
		mes.getChatStyle().setColor(color);
		Minecraft.getMinecraft().thePlayer.addChatMessage(mes);
	}
}
