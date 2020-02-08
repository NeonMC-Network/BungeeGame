package me.naptie.bungee.game.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import me.naptie.bungee.game.Messages;
import me.naptie.bungee.game.utils.CU;
import me.naptie.bungee.game.utils.ServerManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Play extends Command {

	static Map<UUID, String> uuidStringMap = new HashMap<>();

	public Play(String command) {
		super(command);
	}

	@SuppressWarnings("StringConcatenationInLoop")
	@Override
	public void execute(CommandSender commandSender, String[] strings) {
		if (commandSender instanceof ProxiedPlayer) {
			ProxiedPlayer player = (ProxiedPlayer) commandSender;
			if (!ServerManager.isInGame(player)) {
				if (strings.length == 0) {
					if (ServerManager.isInLobby(player, true)) {
						String serverName = player.getServer().getInfo().getName();
						String ints = serverName.replaceAll("\\D+", "");
						strings = new String[]{serverName.replaceAll(ints, "")};
					} else {
						player.sendMessage(CU.c(Messages.getMessage(player, "GAME_ASSIGNATION_REQUIRED")));
					}
				}
				if (strings.length == 1) {
					String server = ServerManager.getGameServer(strings[0], null, false);
					if (server != null && ServerManager.isAbleToJoin(server)) {
						player.connect(ProxyServer.getInstance().getServerInfo(server));
						uuidStringMap.put(player.getUniqueId(), server);
					} else {
						if (ServerManager.isInLobby(player, true)) {
							String serverName = player.getServer().getInfo().getName();
							String ints = serverName.replaceAll("\\D+", "");
							strings = new String[]{serverName.replaceAll(ints, ""), strings[0]};
						} else {
							player.sendMessage(CU.c(Messages.getMessage(player, "SERVER_NOT_FOUND")));
							String games = "";
							for (String game : ServerManager.getGames()) {
								if (games.equals("")) {
									games = game;
								} else {
									games = games + ", " + game;
								}
							}
							player.sendMessage(CU.c(Messages.getMessage(player, "AVAILABLE_GAMES") + games));
							for (String game : ServerManager.getGames())
								player.sendMessage(CU.c(Objects.requireNonNull(Messages.getMessage(player, "AVAILABLE_TYPES_FOR")).replace("%game%", game) + ServerManager.getGameTypes(game)));
						}
					}
				}
				if (strings.length == 2) {
					ServerManager.GameType gameType;
					try {
						gameType = ServerManager.GameType.valueOf(strings[1].toUpperCase());
					} catch (IllegalArgumentException e) {
						player.sendMessage(CU.c(Objects.requireNonNull(Messages.getMessage(player, "TYPE_NOT_FOUND")).replace("%type%", strings[1])));
						return;
					}
					String server = ServerManager.getGameServer(strings[0], gameType, false);
					if (server != null && ServerManager.isAbleToJoin(server)) {
						player.connect(ProxyServer.getInstance().getServerInfo(server));
						uuidStringMap.put(player.getUniqueId(), server);
					} else {
						player.sendMessage(CU.c(Messages.getMessage(player, "SERVER_NOT_FOUND")));
						String games = "";
						for (String game : ServerManager.getGames()) {
							if (games.equals("")) {
								games = game;
							} else {
								games = games + ", " + game;
							}
						}
						player.sendMessage(CU.c(Messages.getMessage(player, "AVAILABLE_GAMES") + games));
						for (String game : ServerManager.getGames())
							player.sendMessage(CU.c(Objects.requireNonNull(Messages.getMessage(player, "AVAILABLE_TYPES_FOR")).replace("%game%", game) + ServerManager.getGameTypes(game)));
					}
				}
			} else {
				player.sendMessage(CU.c(Messages.getMessage(player, "NOT_IN_LOBBY")));
			}
		} else {
			commandSender.sendMessage(CU.c(Messages.getMessage("zh-CN", "NOT_A_PLAYER")));
		}
	}
}
