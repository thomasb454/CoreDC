package net.dancraft.coredc;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

public class SBEntry {
	
	private String name;
	private String value;
	private Team team;
	private Score score;
	
	public SBEntry(String name, String value) {
		this.name = name;
		this.value = value;
		this.team = Scoreboards.board.registerNewTeam(name);
		team.setSuffix(value);
		team.addPlayer(Bukkit.getOfflinePlayer(name));
		score = Scoreboards.obj.getScore(name);
		Scoreboards.entries.add(this);
	}
	
	public String getName() {
		return this.name;
	}
	
	public Score getScore(){
		return this.score;
	}
}
