package net.mcnations.hub;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.mcnations.hub.events.BungeeEventManager;
import net.mcnations.hub.events.EventManager;
import net.mcnations.hub.player.HubPlayer;
import net.mcnations.hub.runnable.ServerStatsUpdater;
import net.md_5.bungee.api.ChatColor;

public class NationsHub extends JavaPlugin {

	private static NationsHub instance;

	// Bukkit events
	private EventManager eventManager;
	// Bungee events
	private BungeeEventManager bungeeEventManager;

	private HashMap<UUID, HubPlayer> hubPlayers;
	private ServerStatsUpdater serverStatsUpdater;
	private ItemStack serverItem;

	@Override
	public void onEnable() {

		System.out.println("[Nations at War Hub] Loading hub plugin...");

		this.eventManager = new EventManager();
		this.bungeeEventManager = new BungeeEventManager();

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", bungeeEventManager);

		Bukkit.getPluginManager().registerEvents(eventManager, this);

		this.hubPlayers = new HashMap<>();
		initItem();

		this.getConfig().options().copyDefaults();
		saveDefaultConfig();

		Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_WEATHER_CYCLE, false);
		Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_MOB_SPAWNING, false);

		instance = this;

		this.serverStatsUpdater = new ServerStatsUpdater();
	}

	@Override
	public void onDisable() {

	}

	public static NationsHub getInstance() {
		return instance;
	}

	public ItemStack getServerItem() {
		return serverItem;
	}

	public Location getHubLocation() {
		return (Location) NationsHub.getInstance().getConfig().get("hubspawn");
	}

	public boolean isAutoConnect() {
		return NationsHub.getInstance().getConfig().getBoolean("autoconnect");
	}

	private void initItem() {
		serverItem = new ItemStack(Material.NETHER_STAR);
		ItemMeta serverItemMeta = serverItem.getItemMeta();
		serverItemMeta.setDisplayName(ChatColor.GREEN + "Servers");
		serverItem.setItemMeta(serverItemMeta);
	}

	public void addHubPlayer(Player player) {
		hubPlayers.put(player.getUniqueId(), new HubPlayer(player));
	}

	public HubPlayer getHubPlayer(Player player) {
		return hubPlayers.get(player.getUniqueId());
	}

	public ServerStatsUpdater getServerStatsUpdater() {
		return serverStatsUpdater;
	}

	public HashMap<UUID, HubPlayer> getHubPlayers() {
		return hubPlayers;
	}
}
