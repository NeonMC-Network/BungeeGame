package me.naptie.bungee.game.utils;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import me.naptie.bungee.game.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ServerManager {

	public static String getName(String server) {
		for (int id : Main.mysql.editor.getAllKeys()) {
			Map<String, Object> m = Main.mysql.editor.get(id);
			if (m.get("server").equals(server)) {
				return (String) m.get("name");
			}
		}
		return null;
	}

	public static String getType(String server) {
		for (int id : Main.mysql.editor.getAllKeys()) {
			Map<String, Object> m = Main.mysql.editor.get(id);
			if (m.get("server").equals(server)) {
				return (String) m.get("type");
			}
		}
		return null;
	}

	public static List<GameType> getGameTypes(String game) {
		List<GameType> gameTypes = new ArrayList<>();
		for (int id : Main.mysql.editor.getAllKeys()) {
			Map<String, Object> m = Main.mysql.editor.get(id);
			if (((String) m.get("type")).equalsIgnoreCase(game)) {
				String serverName = (String) m.get("server");
				int perteam = (int) m.get("perteam");
				GameType gameType;
				if (perteam == 1) gameType = GameType.SOLO;
				else if (perteam == 2) gameType = GameType.DOUBLE;
				else if (perteam == 3) gameType = GameType.TRIPLE;
				else if (perteam == 4) gameType = GameType.QUADRUPLE;
				else if (perteam > 4) gameType = GameType.MEGA;
				else gameType = GameType.CUSTOM;
				if (!gameTypes.contains(gameType) && (serverName.contains("mini") || serverName.contains("mega"))) {
					gameTypes.add(gameType);
				}
			}
		}
		return gameTypes;
	}

	public static List<String> getGames() {
		List<String> games = new ArrayList<>();
		for (int id : Main.mysql.editor.getAllKeys()) {
			Map<String, Object> m = Main.mysql.editor.get(id);
			String game = (String) m.get("type");
			String serverName = (String) m.get("server");
			if (!games.contains(game) && (serverName.contains("mini") || serverName.contains("mega"))) {
				games.add(game);
			}
		}
		return games;
	}

	public static String getGameServer(String game, GameType type, boolean spectate) {
		try {
			List<Integer> games = new ArrayList<>();
			for (int id : Main.mysql.editor.getAllKeys()) {
				Map<String, Object> m = Main.mysql.editor.get(id);
				if (((String) m.get("type")).equalsIgnoreCase(game)
						&& (((String) m.get("server")).toLowerCase().contains("mini")
						|| ((String) m.get("server")).toLowerCase().contains("mega"))) {
					games.add(id);
				}
			}
			for (int id1 : games) {
				Map<String, Object> m1 = Main.mysql.editor.get(games.get(id1));
				if (spectate) {
					if ((int) m1.get("players") + (int) m1.get("spectators") >= (int) m1.get("max")
							|| ((String) m1.get("state")).equalsIgnoreCase("lobby")
							|| ((String) m1.get("state")).equalsIgnoreCase("starting")
							|| ((String) m1.get("state")).equalsIgnoreCase("ending")) {
						continue;
					}
				} else {
					if ((int) m1.get("players") >= (int) m1.get("max")
							|| !(((String) m1.get("state")).equalsIgnoreCase("lobby")
							|| ((String) m1.get("state")).equalsIgnoreCase("starting"))) {
						continue;
					}
				}
				if (type == GameType.SOLO) {
					if ((int) m1.get("perteam") == 1) {
						return (String) m1.get("server");
					}
				}
				if (type == GameType.DOUBLE) {
					if ((int) m1.get("perteam") == 2) {
						return (String) m1.get("server");
					}
				}
				if (type == GameType.TRIPLE) {
					if ((int) m1.get("perteam") == 3) {
						return (String) m1.get("server");
					}
				}
				if (type == GameType.QUADRUPLE) {
					if ((int) m1.get("perteam") == 4) {
						return (String) m1.get("server");
					}
				}
				if (type == GameType.MEGA) {
					if (((String) m1.get("server")).contains("mega")) {
						return (String) m1.get("server");
					}
				}
			}
			if (type == null) {
				int i = ThreadLocalRandom.current().nextInt(0, games.toArray().length);
				return (String) Main.mysql.editor.get(games.get(i)).get("server");
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	public static String getLobby(String game) {
		for (int id : Main.mysql.editor.getAllKeys()) {
			Map<String, Object> m = Main.mysql.editor.get(id);
			if (m.get("server").equals(game)) {
				String type = (String) m.get("type");
				List<Integer> lobbies = new ArrayList<>();
				for (int id1 : Main.mysql.editor.getAllKeys()) {
					Map<String, Object> m1 = Main.mysql.editor.get(id1);
					String name = (String) m1.get("name");
					if (!((String) m1.get("state")).equalsIgnoreCase("ending") && ((Integer) m1.get("players")) < ((Integer) m1.get("max")) && m1.get("type").equals(type) && name.toLowerCase().contains("lobby")) {
						lobbies.add(id1);
					}
				}
				int i = ThreadLocalRandom.current().nextInt(0, lobbies.toArray().length);
				return (String) Main.mysql.editor.get(lobbies.get(i)).get("server");
			}
		}
		return null;
	}

	public static boolean isInLobby(ProxiedPlayer player, boolean gamingLobbyRequired) {
		String serverName = player.getServer().getInfo().getName();
		for (String game : getGames()) {
			if (serverName.toLowerCase().contains(game.toLowerCase()))
				return true;
		}
		if (gamingLobbyRequired) {
			return false;
		} else {
			return serverName.contains("lobby");
		}
	}

	public static boolean isInMainLobby(ProxiedPlayer player) {
		boolean result = false;
		for (int id : Main.mysql.editor.getAllKeys()) {
			Map<String, Object> m = Main.mysql.editor.get(id);
			if (m.get("type").equals("Lobby")) {
				if (player.getServer().getInfo().getName().equals(m.get("server"))) {
					result = true;
				}
			}
		}
		return result;
	}

	public static boolean isInGame(ProxiedPlayer player) {
		String serverName = player.getServer().getInfo().getName();
		return serverName.contains("mini") || serverName.contains("mega");
	}

	public static boolean isAbleToJoin(String server) {
		for (int id : Main.mysql.editor.getAllKeys()) {
			Map<String, Object> m = Main.mysql.editor.get(id);
			if (m.get("server").equals(server)) {
				return ((int) m.get("players") < (int) m.get("max")) && (((String) m.get("state")).equalsIgnoreCase("lobby") || ((String) m.get("state")).equalsIgnoreCase("starting"));
			}
		}
		return false;
	}

	public static boolean isAbleToSpectate(String server) {
		for (int id : Main.mysql.editor.getAllKeys()) {
			Map<String, Object> m = Main.mysql.editor.get(id);
			if (m.get("server").equals(server)) {
				return ((int) m.get("players") + (int) m.get("spectators") < (int) m.get("max")) && !(((String) m.get("state")).equalsIgnoreCase("lobby") || ((String) m.get("state")).equalsIgnoreCase("starting") || ((String) m.get("state")).equalsIgnoreCase("ending"));
			}
		}
		return false;
	}

	public enum GameType {
		SOLO, DOUBLE, TRIPLE, QUADRUPLE, MEGA, CUSTOM
	}

}
