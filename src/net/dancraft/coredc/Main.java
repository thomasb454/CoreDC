package net.dancraft.coredc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import net.dancraft.dcprison.Rank;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.massivecraft.factions.entity.Board;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.listeners.FactionsListenerMain;
import com.massivecraft.massivecore.ps.PS;

public class Main extends JavaPlugin implements Listener {

	public static Main plugin;

	public static ArrayList<Material> ores = new ArrayList<Material>();

	public void onEnable() {
		plugin = this;
		FileConfiguration config = plugin.getConfig();
		this.getServer().getPluginManager().registerEvents(this, this);
		config.addDefault("Factions.State", false);
		config.addDefault("Prison.State", false);
		config.options().copyDefaults(true);
		saveConfig();
		Vault.runVault();
		// updateScoreboard();
		oresGen();
	}

	public void onDisable() {

	}

	public void updateScoreboard() {
		new BukkitRunnable() {
			@SuppressWarnings({ "unchecked", "unused" })
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					drawScoreboard(p);
				}
			}
		}.runTaskTimer(this, 0, 100);
	}

	public void oresGen() {
		ores.addAll(Arrays.asList(Material.GOLD_ORE, Material.REDSTONE_ORE, Material.LAPIS_ORE, Material.STONE, Material.IRON_ORE, Material.GOLD_ORE, Material.DIAMOND, Material.EMERALD));
	}

	@EventHandler
	public void breakBlock(BlockBreakEvent e) {
		Block block = e.getBlock();
		if (ores.contains(block.getType())) {
			int blockId = block.getTypeId();
			e.getBlock().setType(Material.AIR);
			Player player = e.getPlayer();
			e.setCancelled(true);
			player.getInventory().addItem(new ItemStack(blockId));
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void playerChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		e.setFormat(ChatColor.translateAlternateColorCodes('&', getPrisonRank(player) + getFaction(player) + getRankChat(player) + "&7" + e.getPlayer().getName() + "&a: &7" + e.getMessage()));
	}

	@EventHandler
	public void playerAnvil(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Block block = e.getClickedBlock();
			if (block.getType() == Material.ANVIL) {
				block.setData((byte) 0);
			}
		}
	}

	public String getFaction(Player player) {
		String message = "";
		FileConfiguration config = plugin.getConfig();
		if (config.getBoolean("Factions.State")) {
			Player p = Bukkit.getPlayerExact(player.getName());
			UPlayer uplayer = UPlayer.get(p);
			if (uplayer.hasFaction()) {
				message = ChatColor.translateAlternateColorCodes('&', "&a[&7" + uplayer.getFactionName() + "&a] ");
				return message;
			} else {
				return message;
			}
		} else {
			return message;
		}
	}

	public String getPrisonRank(Player player) {
		String message = "";
		FileConfiguration config = plugin.getConfig();
		if (config.getBoolean("Prison.State")) {
			String rank = Rank.getPlayerRank(player).getRankName();
			message = ChatColor.translateAlternateColorCodes('&', "&a[&7" + rank + "&a] ");
			return message;
		} else {
			return message;
		}
	}

	public String getRankChat(Player player) {
		String message = "";
		if (player.hasPermission("rank.iron")) {
			message = ChatColor.translateAlternateColorCodes('&', "&a[&8Iron&a] ");
		} else if (player.hasPermission("rank.gold")) {
			message = ChatColor.translateAlternateColorCodes('&', "&a[&6Gold&a] ");
		} else if (player.hasPermission("rank.diamond")) {
			message = ChatColor.translateAlternateColorCodes('&', "&a[&3Diamond&a] ");
		} else if (player.hasPermission("rank.bedrock")) {
			message = ChatColor.translateAlternateColorCodes('&', "&a[&4Bedrock&a] ");
		}
		return message;
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		Scoreboards.createScoreboard(player);
		// drawScoreboard(player);
	}

	public void drawScoreboard(Player player) {

	}

}
