package me.naptie.bungee.game;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;
import me.naptie.bungee.game.utils.CU;

import java.io.File;
import java.io.IOException;

public class Messages {

	private static File playerDataFolder;

	@SuppressWarnings("ResultOfMethodCallIgnored")
	static void initialize() {
		playerDataFolder = new File(Main.getInstance().getDataFolder().getAbsolutePath().split("NeonMC" + (System.getProperty("os.name").startsWith("Windows") ? "\\\\" : File.separator))[0] + "NeonMC" + File.separator + "PlayerData" + File.separator);
		if (!playerDataFolder.exists()) {
			playerDataFolder.mkdir();
		}
	}

	public static String getMessage(Configuration language, String message) {
		return CU.ts(language.getString(message));
	}

	public static String getMessage(String language, String message) {
		try {
			return CU.ts(YamlConfiguration.getProvider(YamlConfiguration.class).load(new File(Main.getInstance().getDataFolder(), language + ".yml")).getString(message));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getMessage(ProxiedPlayer player, String message) {
		try {
			return CU.ts(getLanguage(player).getString(message));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Configuration getLanguage(ProxiedPlayer player) throws IOException {
		File locale = new File(Main.getInstance().getDataFolder(), getLanguageName(player) + ".yml");
		return YamlConfiguration.getProvider(YamlConfiguration.class).load(locale);
	}

	private static String getLanguageName(ProxiedPlayer player) throws IOException {
		File file = new File(playerDataFolder, player.getUniqueId() + ".yml");
		Configuration config = YamlConfiguration.getProvider(YamlConfiguration.class).load(file);
		return config.getString("language");
	}

}
