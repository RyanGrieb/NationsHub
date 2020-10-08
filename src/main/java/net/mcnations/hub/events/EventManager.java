package net.mcnations.hub.events;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import net.mcnations.hub.NationsHub;
import net.mcnations.hub.inventory.ServersInventory;
import net.mcnations.hub.player.HubPlayer;
import net.md_5.bungee.api.ChatColor;

public class EventManager implements Listener {

	@EventHandler
	public void playerJoinEvent(final PlayerJoinEvent event) {
		// TODO: Send players to game if it is running & config option is enabled

		final Player player = event.getPlayer();

		player.setGameMode(GameMode.ADVENTURE);
		player.getInventory().clear();
		player.teleport(NationsHub.getInstance().getHubLocation());

		if (NationsHub.getInstance().isAutoConnect()) {
			return;
		}

		player.getInventory().setItem(0, NationsHub.getInstance().getServerItem());

		NationsHub.getInstance().addHubPlayer(player);

		player.sendMessage(ChatColor.RED + "Right click the nether star to see available servers!");

		HubPlayer hubPlayer = NationsHub.getInstance().getHubPlayer(event.getPlayer());
		hubPlayer.setServersInventory(new ServersInventory(event.getPlayer()));
	}

	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		ItemStack item = event.getItem();
		if (item == null)
			return;

		if (item.equals(NationsHub.getInstance().getServerItem())) {
			HubPlayer hubPlayer = NationsHub.getInstance().getHubPlayer(event.getPlayer());
			hubPlayer.setServersInventory(new ServersInventory(event.getPlayer()));
		}
	}

	@EventHandler
	public void inventoryCloseEvent(InventoryCloseEvent event) {
		HubPlayer hubPlayer = NationsHub.getInstance().getHubPlayer((Player) event.getPlayer());
		hubPlayer.setServersInventory(null);
	}

	@EventHandler
	public void playerDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player)
			event.setCancelled(true);
	}

	@EventHandler
	public void playerDropItemEvent(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent event) {
		HubPlayer hubPlayer = NationsHub.getInstance().getHubPlayer((Player) event.getWhoClicked());

		if (event.getClickedInventory() != null && hubPlayer.getServersInventory() != null) {
			if (event.getClickedInventory().equals(hubPlayer.getServersInventory().getInv())) {
				hubPlayer.getServersInventory().clickEvent(event);
			}
		}

		if (!event.getWhoClicked().isOp()) {
			event.setCancelled(true);
			hubPlayer.getBukkitPlayer().updateInventory();
		}
	}

	@EventHandler
	public void foodLevelChangeEvent(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		if (player.getLocation().getY() < 0)
			player.teleport(NationsHub.getInstance().getHubLocation());
	}
	
	@EventHandler
	public void playerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
		event.setCancelled(true);
	}
}
