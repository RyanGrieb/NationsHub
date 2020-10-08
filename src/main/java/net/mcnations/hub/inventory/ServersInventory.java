package net.mcnations.hub.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.mcnations.hub.NationsHub;
import net.mcnations.hub.runnable.ServerStatContainer;

public class ServersInventory {

	private Player player;
	private Inventory inventory;

	public ServersInventory(Player player) {
		this.player = player;
		this.inventory = generateInventory();

		// FIXME: Best approach, feels lazy
		onStatUpdate();

		player.openInventory(inventory);
	}

	public void onStatUpdate() {
		for (int i = 0; i < inventory.getContents().length; i++) {
			ItemStack item = inventory.getContents()[i];

			if (item == null)
				continue;

			// Call the server from the hashmap

			ServerStatContainer serverStat = NationsHub.getInstance().getServerStatsUpdater()
					.getServerStats(ChatColor.stripColor(item.getItemMeta().getDisplayName().toLowerCase()));

			if (serverStat != null) {
				ItemMeta itemMeta = item.getItemMeta();

				List<String> lore = new ArrayList<>();
				if (serverStat.isOnline()) {
					lore.add(ChatColor.GREEN + "ONLINE");
				} else {
					lore.add(ChatColor.RED + "OFFLINE");
				}

				lore.add(ChatColor.YELLOW + "Players: " + ChatColor.WHITE + serverStat.getPlayerCount());
				itemMeta.setLore(lore);

				item.setItemMeta(itemMeta);
				inventory.setItem(i, item);
			}
		}

		NationsHub.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(NationsHub.getInstance(),
				new Runnable() {
					@Override
					public void run() {
						player.updateInventory();
					}
				}, 1L);
	}

	public void clickEvent(InventoryClickEvent event) {
		final ItemStack item = event.getCurrentItem();

		if (item == null || !item.hasItemMeta())
			return;

		String serverName = ChatColor.stripColor(item.getItemMeta().getDisplayName().toLowerCase());
		ServerStatContainer serverStat = NationsHub.getInstance().getServerStatsUpdater().getServerStats(serverName);

		if (!serverStat.isOnline())
			return;

		NationsHub.getInstance().getServer().getScheduler().runTask(NationsHub.getInstance(), new Runnable() {
			@Override
			public void run() {
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("Connect");
				out.writeUTF(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
				player.sendPluginMessage(NationsHub.getInstance(), "BungeeCord", out.toByteArray());
			}
		});
	}

	private Inventory generateInventory() {
		Inventory inventory = Bukkit.createInventory(player, 9, "Servers");

		// TODO: Check bungee for list of servers
		ItemStack nationsServerItem = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta nationsServerItemMeta = nationsServerItem.getItemMeta();
		nationsServerItemMeta.setDisplayName(ChatColor.BLUE + "Nations");
		List<String> lore = new ArrayList<>();
		nationsServerItemMeta.setLore(lore);
		nationsServerItem.setItemMeta(nationsServerItemMeta);
		inventory.setItem(0, nationsServerItem);

		return inventory;
	}

	public Inventory getInv() {
		return inventory;
	}

}
