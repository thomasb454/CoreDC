package net.dancraft.coredc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
	}

	public void onDisable() {

	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void playerChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		e.setFormat(ChatColor.translateAlternateColorCodes('&', getFaction(player) + "&7" + e.getPlayer().getName() + "&a: &7" + e.getMessage()));
	}

	public String getFaction(Player player) {
		String message = "";
		FileConfiguration config = plugin.getConfig();
		if (config.getBoolean("Factions.State")) {
			Player p = Bukkit.getPlayerExact(player.getName());
			UPlayer uplayer = UPlayer.get(p);
			if(uplayer.hasFaction()){
				message = ChatColor.translateAlternateColorCodes('&', "&a[&7" + uplayer.getFactionName() + "&a] ");
				return message;
			} else {
				return message;
			}
		} else {
			return message;
		}

	}

}
