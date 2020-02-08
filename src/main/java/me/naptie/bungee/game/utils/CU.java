package me.naptie.bungee.game.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class CU {

	public static TextComponent ttc(String message) {
		return new TextComponent(ts(message));
	}

	public static TextComponent c(String message) {
		return new TextComponent(message);
	}

	public static String ts(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

}
