package net.mcnations.hub.runnable;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.mcnations.hub.NationsHub;
import net.mcnations.hub.player.HubPlayer;

public class ServerStatsUpdater {

	private StatsRunnable statsRunnable;
	private HashMap<String, ServerStatContainer> serverStats;

	public ServerStatsUpdater() {
		this.statsRunnable = new StatsRunnable();
		
		statsRunnable.runTaskTimer(NationsHub.getInstance(), 0, 20 * 5);
		this.serverStats = new HashMap<>();
	}

	private class StatsRunnable extends BukkitRunnable {

		@Override
		public void run() {
			// FIXME: This accounts for a single server instance
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("PlayerCount");
			out.writeUTF("Nations");
			Bukkit.getServer().sendPluginMessage(NationsHub.getInstance(), "BungeeCord", out.toByteArray());

			// Ping server, by default it is offline
			// updateServerOnlineStatus("nations", false);

			try {
				Socket socket = new Socket();
				socket.connect(new InetSocketAddress("localhost", 25567), 15); // 15 is timeout
				socket.close();

				updateServerOnlineStatus("nations", true);
			} catch (Exception e) {
				updateServerOnlineStatus("nations", false);
			}

		}
	}

	public void updateServerPlayercount(String server, int playerCount) {
		ServerStatContainer serverStat = serverStats.get(server);

		if (serverStat == null) {
			serverStats.put(server, new ServerStatContainer(false, playerCount));
		} else {
			serverStat.setPlayerCount(playerCount);
		}

		for (HubPlayer hubPlayer : NationsHub.getInstance().getHubPlayers().values()) {
			if (hubPlayer.getServersInventory() != null)
				hubPlayer.getServersInventory().onStatUpdate();
		}
	}

	public void updateServerOnlineStatus(String server, boolean online) {
		ServerStatContainer serverStat = serverStats.get(server);

		if (serverStat == null) {
			serverStats.put(server, new ServerStatContainer(false, 0));
		} else {
			serverStat.setOnline(online);
		}

		for (HubPlayer hubPlayer : NationsHub.getInstance().getHubPlayers().values()) {
			if (hubPlayer.getServersInventory() != null)
				hubPlayer.getServersInventory().onStatUpdate();
		}
	}

	public ServerStatContainer getServerStats(String serverName) {
		return serverStats.get(serverName);
	}
}
