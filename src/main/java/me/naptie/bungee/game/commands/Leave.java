package me.naptie.bungee.game.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import me.naptie.bungee.game.Messages;
import me.naptie.bungee.game.utils.CU;
import me.naptie.bungee.game.utils.ServerManager;

public class Leave extends Command {

	public Leave(String command) {
		super(command);
	}

	@Override
	public void execute(CommandSender commandSender, String[] strings) {
		if (commandSender instanceof ProxiedPlayer) {
			ProxiedPlayer player = (ProxiedPlayer) commandSender;
			if (ServerManager.isInGame(player)) {
				String lobby = ServerManager.getLobby(player.getServer().getInfo().getName());
				player.connect(ProxyServer.getInstance().getServerInfo(lobby));
			} else {
				player.sendMessage(CU.c(Messages.getMessage(player, "NOT_IN_GAME")));
			}
		} else {
			commandSender.sendMessage(CU.c(Messages.getMessage("zh-CN", "NOT_A_PLAYER")));
		}
	}
}
