package net.dancraft.coredc;

import net.dancraft.dcprison.Rank;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Scoreboards {

	public static ArrayList<SBEntry> entries = new ArrayList<SBEntry>();

	public static Scoreboard board;
	public static Objective obj;

	public static void createScoreboard(Player player) {
		board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		player.setScoreboard(board);

		obj = board.registerNewObjective("Scoreboard", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lPRISON"));
		
		new SBEntry(ChatColor.YELLOW + "TEST", "1");
		
		int loop = entries.size();
		for (SBEntry entry : entries) {
			entry.getScore().setScore(loop);
			loop--;
		}

		player.setScoreboard(board);
	}

	public static void newScore(String name, String value) {

	}
	
	/*Team balance = board.registerNewTeam(ChatColor.GREEN + "Balance: ");
	balance.setSuffix("" + ChatColor.GRAY + Vault.economy.getBalance(player));
	balance.addPlayer(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Balance: "));
	Score balanceTest = obj.getScore(ChatColor.GREEN + "Balance: ");
	Score kills = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&aKills: &70"));
	Score deaths = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&aDeaths: &70"));
	Score space1 = obj.getScore(" ");
	Score space2 = obj.getScore("  ");
	Score space3 = obj.getScore("   ");
	Rank pRank = Rank.getPlayerRank(player);
	Score rank = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&aRank: &7" + pRank.getRankName()));
	Score nextRank = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&aNext: &7" + pRank.getNextRank().getRankName()));
	Score nextRankPrice = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&aCost: &7" + pRank.getNextRank().getPrice()));*/
}