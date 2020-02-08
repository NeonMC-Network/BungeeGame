package me.naptie.bungee.game;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import me.naptie.bungee.game.commands.Leave;
import me.naptie.bungee.game.commands.Lobby;
import me.naptie.bungee.game.commands.Play;
import me.naptie.bungee.game.commands.Rejoin;
import me.naptie.bungee.game.utils.MySQLManager;

import java.io.*;
import java.util.logging.Logger;

public class Main extends Plugin {

	public static Configuration config;
	public static MySQLManager mysql;
	private static Logger logger;
	private static Main instance;

	static Main getInstance() {
		return instance;
	}

	@SuppressWarnings({"ResultOfMethodCallIgnored"})
	@Override
	public void onEnable() {
		instance = this;
		logger = this.getLogger();

		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}

		saveResource("config.yml", false);
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String language : config.getStringList("languages")) {
			File localeFile = new File(getDataFolder(), language + ".yml");
			if (localeFile.exists()) {
				if (config.getBoolean("update-language-files")) {
					saveResource(language + ".yml", true);
				}
			} else {
				saveResource(language + ".yml", false);
			}
		}

		mysql = new MySQLManager();

		getProxy().getPluginManager().registerCommand(this, new Lobby());
		getProxy().getPluginManager().registerCommand(this, new Leave("leave"));
		getProxy().getPluginManager().registerCommand(this, new Play("play"));
		getProxy().getPluginManager().registerCommand(this, new Rejoin("rejoin"));

		Messages.initialize();
	}

	@Override
	public void onDisable() {
		instance = null;
		logger = null;
	}

	@SuppressWarnings({"ResultOfMethodCallIgnored", "UnstableApiUsage"})
	private void saveResource(String fileName, boolean replace) {
		File file = new File(getDataFolder(), fileName);
		if (!file.exists() || replace) {
			try {
				file.createNewFile();
				try (InputStream is = getResourceAsStream(fileName);
				     OutputStream os = new FileOutputStream(file)) {
					ByteStreams.copy(is, os);
				}
			} catch (IOException e) {
				throw new RuntimeException("Unable to create configuration file", e);
			}
		}
	}
}
