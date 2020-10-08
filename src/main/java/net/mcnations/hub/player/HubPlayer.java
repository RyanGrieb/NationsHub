package net.mcnations.hub.player;

import org.bukkit.entity.Player;

import net.mcnations.hub.inventory.ServersInventory;

public class HubPlayer {

	private Player player;
	private ServersInventory serversInventory;

	public HubPlayer(Player player) {
		this.player = player;
	}

	public ServersInventory getServersInventory() {
		return serversInventory;
	}

	public void setServersInventory(ServersInventory serversInventory) {
		this.serversInventory = serversInventory;
	}

	public Player getBukkitPlayer() {
		return player;
	}

}
