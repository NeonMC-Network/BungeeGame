package me.naptie.bungee.game.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import me.naptie.bungee.game.Messages;
import me.naptie.bungee.game.utils.CU;
import me.naptie.bungee.game.utils.ServerManager;

import java.util.Objects;

public class Lobby extends Command {

	public Lobby() {
		super("lobby", null, "hub", "gotolobby", "gotohub");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof ProxiedPlayer) {
			ProxiedPlayer player = (ProxiedPlayer) sender;
			if (ServerManager.isInMainLobby(player)) {
				player.sendMessage(CU.c(Messages.getMessage(player, "ALREADY_IN_MAIN_LOBBY")));
			} else {
				String lobby;
				try {
					lobby = ServerManager.getLobby("lobby01");
				} catch (Exception e) {
					lobby = "lobby01";
				}
				player.connect(ProxyServer.getInstance().getServerInfo(lobby));
				player.sendMessage(CU.c(Objects.requireNonNull(Messages.getMessage(player, "SENDING")).replace("%server%", Objects.requireNonNull(lobby))));
			}
		} else {
			sender.sendMessage(CU.c(Messages.getMessage("zh-CN", "NOT_A_PLAYER")));
		}
	}

}
