package net.dancraft.coredc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
 
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
 
public class Scoreboard
{
    private org.bukkit.scoreboard.Scoreboard scoreboard;
    private HashMap<Integer, Page> pages = new HashMap<>();
    private int pageSwitchTime;
    private int currentPage = -1;
 
    public Scoreboard(Player player, int pageSwitchTime)
    {
        this.pageSwitchTime = pageSwitchTime;
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(scoreboard);
    }
 
    public void showPage(int page)
    {
        if (currentPage > -1) getPage(currentPage).hide();
        currentPage = page;
        getPage(page).display();
    }
 
    public int getCurrentPage()
    {
        return currentPage;
    }
 
    public void startShow()
    {
        new BukkitRunnable()
        {
            int pageIndex = 0;
 
            @Override
            public void run()
            {
                if (pages.isEmpty()) return;
                getPage(pageIndex).hide();
                pageIndex++;
                if (pageIndex > pages.size() - 1) pageIndex = 0;
                getPage(pageIndex).display();
            }
        }.runTaskTimer(Main.plugin, pageSwitchTime, pageSwitchTime);
    }
 
    public void addPage(Page page)
    {
        page.setScoreboard(scoreboard);
        pages.put(page.getId(), page);
    }
 
    public void removePage(int id)
    {
        pages.remove(id);
    }
 
    public Page getPage(int id)
    {
        return pages.get(id);
    }
 
    public HashMap<Integer, Page> getPages()
    {
        return pages;
    }
 
    public static class Page
    {
        private Map<String, Entry> entries = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        private String objectiveName;
        private String title;
        private int id;
        private boolean isShown = false;
        private org.bukkit.scoreboard.Scoreboard scoreboard;
 
        public Page(int id, String title)
        {
            objectiveName = String.valueOf(id);
            this.id = id;
            this.title = title;
        }
 
        private void setScoreboard(org.bukkit.scoreboard.Scoreboard scoreboard)
        {
            this.scoreboard = scoreboard;
            Objective obj = scoreboard.getObjective(objectiveName);
            if (obj != null) obj.unregister();
        }
 
        public Map<String, Entry> getEntries()
        {
            return entries;
        }
 
        public int getId()
        {
            return id;
        }
 
        public String getTitle()
        {
            return title;
        }
 
        public void addNewEntry(String name, Page.Entry entry)
        {
            if (!entries.containsKey(name))
            {
                entries.put(name, entry);
                if (isShown) display();
            }
            else setValue(name, entry.getValue());
        }
 
        public boolean setValue(String entryName, Object value)
        {
            if (!entries.containsKey(entryName)) return false;
            entries.get(entryName).setValue(value);
            if (isShown) update();
            return true;
        }
 
        private ChatColor getColor(int colorIndex)
        {
            try
            {
                Field field = ChatColor.class.getDeclaredField("BY_ID");
                field.setAccessible(true);
                Map<Integer, ChatColor> BY_ID = (Map<Integer, ChatColor>) field.get(null);
                return BY_ID.get(colorIndex);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
 
        private void hide()
        {
            Objective objective = scoreboard.getObjective(objectiveName);
            if (objective != null) objective.unregister();
            isShown = false;
        }
 
        private void display()
        {
            scoreboard.clearSlot(DisplaySlot.SIDEBAR);
            isShown = true;
            int i = entries.size() * 3;//Including the * 3 for blank line & value.
            int uniqueColorIndex = 1;
            scoreboard.clearSlot(DisplaySlot.SIDEBAR);
            Objective objective = scoreboard.getObjective(objectiveName);
            if (objective != null) objective.unregister();
            objective = scoreboard.registerNewObjective(objectiveName, "dummy");
            objective.setDisplayName(title);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            for (Page.Entry entry : entries.values())
            {
                ChatColor uniqueColor = getColor(uniqueColorIndex);
                objective.getScore(entry.keyPlayer = Bukkit.getOfflinePlayer(entry.getKey())).setScore(i);
                i--;
                objective.getScore(entry.valuePlayer = Bukkit.getOfflinePlayer(entry.getValue() + uniqueColor)).setScore(i);
                i--;
                if (i > 1)
                {
                    objective.getScore(Bukkit.getOfflinePlayer("" + uniqueColor)).setScore(i);
                    i--;
                }
                uniqueColorIndex++;
            }
            objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY + "--------------")).setScore(i);
        }
 
        private void update()
        {
            Objective objective = scoreboard.getObjective(objectiveName);
            for (Page.Entry entry : entries.values())
            {
                if (entry.valuePlayer.getName().equalsIgnoreCase(entry.getValue())) continue;
                int valueLocation = objective.getScore(entry.valuePlayer).getScore();
                scoreboard.resetScores(entry.valuePlayer);
                objective.getScore(entry.valuePlayer = Bukkit.getOfflinePlayer(entry.getValue())).setScore(valueLocation);
            }
        }
 
        public static class Entry
        {
            private String key;
            private Object value;
            private OfflinePlayer keyPlayer;
            private OfflinePlayer valuePlayer;
 
            public Entry(String key, Object value)
            {
                this.key = key;
                this.value = value;
            }
 
            public String getKey()
            {
                String key = this.key;
                if (!key.contains("§")) key = ChatColor.YELLOW + key;
                return key;
            }
 
            public String getValue()
            {
                String value = this.value.toString();
                if (!value.contains("§")) value = ChatColor.GRAY + value;
                return value;
            }
 
            public void setValue(Object value)
            {
                this.value = value;
            }
        }
    }
}