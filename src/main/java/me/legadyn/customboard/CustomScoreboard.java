package me.legadyn.customboard;

import java.text.DecimalFormat;
import java.util.List;

import me.tigerhix.lib.scoreboard.ScoreboardLib;
import me.tigerhix.lib.scoreboard.common.EntryBuilder;
import me.tigerhix.lib.scoreboard.common.Strings;
import me.tigerhix.lib.scoreboard.common.animate.HighlightedString;
import me.tigerhix.lib.scoreboard.common.animate.ScrollableString;
import me.tigerhix.lib.scoreboard.type.Entry;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import me.tigerhix.lib.scoreboard.type.ScoreboardHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Objective;

public class CustomScoreboard implements Listener {

    public static Scoreboard scoreboard;

    Main main;

    static int seconds;

    static int minutes;

    static int hours;

    FileConfiguration config;

    DecimalFormat df = new DecimalFormat("00");

    org.bukkit.scoreboard.Scoreboard scoreboardserver;

    public CustomScoreboard(Main main, int seconds, int minutes, int hours, FileConfiguration config) {
        this.main = main;
        CustomScoreboard.seconds = seconds;
        CustomScoreboard.minutes = minutes;
        CustomScoreboard.hours = hours;
        this.config = config;
        startTimer();
    }

    public void createScoreboard() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            scoreboard = ScoreboardLib.createScoreboard(player).setHandler(new ScoreboardHandler() {
                private final ScrollableString scroll = new ScrollableString(Strings.format("&aThis string is scrollable!"), 10, 0);

                private final HighlightedString highlighted = new HighlightedString(CustomScoreboard.this.config.getString("Title"), CustomScoreboard.this.config.getString("Colors.title.primary"), CustomScoreboard.this.config.getString("Colors.title.glow"));

                public String getTitle(Player player) {
                    return this.highlighted.next();
                }

                public List<Entry> getEntries(Player player) {
                    if (!CustomScoreboard.this.config.getBoolean("SaveData.show"))
                        return (new EntryBuilder()).build();

                    return (new EntryBuilder())
                            .next(ChatColor.translateAlternateColorCodes('&', CustomScoreboard.this.config.getString("Lines.1")).replace("_", " "))
                            .next(ChatColor.translateAlternateColorCodes('&', CustomScoreboard.this.config.getString("Lines.2")).replace("_", " "))
                            .next(ChatColor.translateAlternateColorCodes('&', CustomScoreboard.this.config.getString("Lines.3")).replace("_", " "))
                            .next(ChatColor.translateAlternateColorCodes('&', CustomScoreboard.this.config.getString("Lines.4")).replace("_", " "))
                            .next(ChatColor.translateAlternateColorCodes('&', CustomScoreboard.this.config.getString("Lines.5")).replace("_", " "))
                            .next(ChatColor.translateAlternateColorCodes('&', CustomScoreboard.this.config.getString("Lines.6")).replace("_", " ") + ChatColor.WHITE + CustomScoreboard.hours + ":" + CustomScoreboard.this.df.format(CustomScoreboard.minutes) + ":" + CustomScoreboard.this.df.format(CustomScoreboard.seconds))
                            .build();
                }
            }).setUpdateInterval(10L);
            scoreboard.activate();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        createScoreboard();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        scoreboard.deactivate();
    }

    private void startTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            public void run() {

                if (CustomScoreboard.this.config.getBoolean("SaveData.stop"))
                    return;
                if (CustomScoreboard.this.config.getString("SaveData.countdown").equals("false")) {
                    CustomScoreboard.this.countup();
                } else {
                    CustomScoreboard.this.countdown();
                }
                CustomScoreboard.this.scoreTimer();
            }
        },  0L, 20L);
    }

    public void scoreTimer() {
        this.scoreboardserver = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = this.scoreboardserver.getObjective("hours");
        Objective objective1 = this.scoreboardserver.getObjective("minutes");
        Objective objective2 = this.scoreboardserver.getObjective("seconds");
        if (objective == null) {
            String dName = "hours";
            objective = this.scoreboardserver.registerNewObjective("hours", "dummy");
            objective.setDisplayName(dName);
        }
        if (objective1 == null) {
            String dName = "minutes";
            objective1 = this.scoreboardserver.registerNewObjective("minutes", "dummy");
            objective1.setDisplayName(dName);
        }
        if (objective2 == null) {
            String dName = "seconds";
            objective2 = this.scoreboardserver.registerNewObjective("seconds", "dummy");
            objective2.setDisplayName(dName);
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            this.scoreboardserver.getObjective("hours").getScore(p.getName()).setScore(hours);
            this.scoreboardserver.getObjective("minutes").getScore(p.getName()).setScore(minutes);
            this.scoreboardserver.getObjective("seconds").getScore(p.getName()).setScore(seconds);
        }
    }

    public void countdown() {
        if (hours == 0 && seconds == 0 && minutes == 0)
            return;
        if (minutes == 0 && seconds == 0) {
            hours--;
            minutes = 59;
            seconds = 60;
        } else if (seconds == 0) {
            minutes--;
            seconds = 60;
        }
        seconds--;
    }

    public void countup() {
        if (hours == this.config.getInt("Timer.Limit.hours") && minutes == this.config.getInt("Timer.Limit.minutes") && seconds == this.config.getInt("Timer.Limit.seconds"))
            return;
        if (minutes == 59 && seconds == 59) {
            hours++;
            minutes = 0;
            seconds = -1;
        } else if (seconds == 59) {
            minutes++;
            seconds = -1;
        }
        seconds++;
    }

}
