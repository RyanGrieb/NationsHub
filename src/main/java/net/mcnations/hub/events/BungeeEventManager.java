package net.mcnations.hub.events;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.mcnations.hub.NationsHub;
import net.mcnations.hub.player.HubPlayer;

public class BungeeEventManager implements PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player player, final byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}

		// For good measure, run it synchronously.
		NationsHub.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(NationsHub.getInstance(),
				new Runnable() {
					@Override
					public void run() {
						ByteArrayDataInput in = ByteStreams.newDataInput(message);
						String subchannel = in.readUTF();

						if (subchannel.equals("PlayerCount")) {
							String server = in.readUTF();
							int playerCount = in.readInt();

							NationsHub.getInstance().getServerStatsUpdater().updateServerPlayercount(server, playerCount);
						}
					}
				}, 1L);
	}

}
