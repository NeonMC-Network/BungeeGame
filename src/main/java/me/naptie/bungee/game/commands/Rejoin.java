package me.naptie.bungee.game.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import me.naptie.bungee.game.Messages;
import me.naptie.bungee.game.utils.CU;
import me.naptie.bungee.game.utils.ServerManager;

public class Rejoin extends Command {

	public Rejoin(String command) {
		super(command);
	}

	@Override
	public void execute(CommandSender commandSender, String[] strings) {
		if (commandSender instanceof ProxiedPlayer) {
			ProxiedPlayer player = (ProxiedPlayer) commandSender;
			if (Play.uuidStringMap.containsKey(player.getUniqueId())) {
				if (ServerManager.isInLobby(player, false)) {
					String server = Play.uuidStringMap.get(player.getUniqueId());
					if (ServerManager.isAbleToJoin(server)) {
						player.connect(ProxyServer.getInstance().getServerInfo(server));
					} else {
						player.sendMessage(CU.c(Messages.getMessage(player, "SERVER_UNAVAILABLE")));
					}
				} else {
					player.sendMessage(CU.c(Messages.getMessage(player, "NOT_IN_LOBBY")));
				}
			} else {
				player.sendMessage(CU.c(Messages.getMessage(player, "NO_GAME_TO_REJOIN")));
			}
		} else {
			commandSender.sendMessage(CU.c(Messages.getMessage("zh-CN", "NOT_A_PLAYER")));
		}
	}
}
