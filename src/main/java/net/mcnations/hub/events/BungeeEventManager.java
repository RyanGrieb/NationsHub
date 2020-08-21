package net.mcnations.hub.events;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.mcnations.hub.NationsHub;
import net.mcnations.hub.player.HubPlayer;

public class BungeeEventManager implements PluginMessageListener {

	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}

		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();

		if (subchannel.equals("PlayerCount")) {
			String server = in.readUTF();
			int playerCount = in.readInt();

			NationsHub.getInstance().getServerStatsUpdater().updateServerPlayercount(server, playerCount);
		}
	}

}
