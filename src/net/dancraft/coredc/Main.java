package net.dancraft.coredc;

import java.util.HashMap;
import java.util.Map.Entry;

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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.massivecraft.factions.entity.UPlayer;

public class Main extends JavaPlugin implements Listener {

	public static Main plugin;

	public void onEnable() {
		plugin = this;
		FileConfiguration config = plugin.getConfig();
		this.getServer().getPluginManager().registerEvents(this, this);
		config.addDefault("Factions.State", false);
		config.options().copyDefaults(true);
		saveConfig();
		Vault.runVault();
		updateScoreboard();
	}

	public void onDisable() {

	}

	public void updateScoreboard() {
		new BukkitRunnable() {
			@SuppressWarnings({ "unchecked", "unused" })
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					((Scoreboard) p.getMetadata("Scoreboard").get(0).value()).getPage(0).setValue("Money", Vault.economy.getBalance(p));
				}
			}
		}.runTaskTimer(this, 0, 100);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void playerChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		e.setFormat(ChatColor.translateAlternateColorCodes('&', getFaction(player) + "&7" + e.getPlayer().getName() + "&a: &7" + e.getMessage()));
	}

	@EventHandler
	public void playerAnvil(PlayerInteractEvent e) {
		if (e.getClickedBlock().getType() == Material.ANVIL) {
			Block block = e.getClickedBlock();
			block.setData((byte) 0);
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

	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		Scoreboard s = new Scoreboard(player, 40);
		player.setMetadata("Scoreboard", new FixedMetadataValue(plugin, s));
		s.addPage(new Scoreboard.Page(0, ChatColor.translateAlternateColorCodes('&', "&a&lPRISON")));
		s.getPage(0).addNewEntry("01", new Scoreboard.Page.Entry("Username", player.getName()));
		s.getPage(0).addNewEntry("02", new Scoreboard.Page.Entry("Balance", Vault.economy.getBalance(player)));
		s.showPage(0);
	}

}
