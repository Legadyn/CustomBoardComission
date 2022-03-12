package me.legadyn.customboard;

import me.tigerhix.lib.scoreboard.ScoreboardLib;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {
    FileConfiguration config = getConfig();

    ScoreboardCommandExecutor executor = new ScoreboardCommandExecutor(this);

    public void onEnable() {

        ScoreboardLib.setPluginInstance(this);

        addDefaults();
        this.config.options().copyDefaults(true);
        saveConfig();

        getCommand("customboard").setExecutor(this.executor);
        getCommand("customboard").setTabCompleter(new ScoreboardCommandCompleter());

        CustomScoreboard scoreboard = new CustomScoreboard(this, this.config.getInt("Timer.seconds"), this.config.getInt("Timer.minutes"), this.config.getInt("Timer.hours"), this.config);

        getServer().getPluginManager().registerEvents(scoreboard, this);
        getServer().getPluginManager().registerEvents(this, this);
        if (!Bukkit.getServer().getOnlinePlayers().isEmpty())
            scoreboard.createScoreboard();

    }

    public void onLoad() {}

    public void onDisable() {
        this.config.set("Timer.hours", Integer.valueOf(CustomScoreboard.hours));
        this.config.set("Timer.minutes", Integer.valueOf(CustomScoreboard.minutes));
        this.config.set("Timer.seconds", Integer.valueOf(CustomScoreboard.seconds));
        saveConfig();
    }

    public void addDefaults() {
        this.config.addDefault("Display", "default");
        this.config.addDefault("Timer.hours", "03");
        this.config.addDefault("Timer.minutes", "30");
        this.config.addDefault("Timer.seconds", "00");
        this.config.addDefault("Timer.Limit.hours", Integer.valueOf(5));
        this.config.addDefault("Timer.Limit.minutes", Integer.valueOf(0));
        this.config.addDefault("Timer.Limit.seconds", Integer.valueOf(0));
        this.config.addDefault("Colors.title.primary", "&6&l");
        this.config.addDefault("Colors.title.glow", "&e&l");
        this.config.addDefault("Title", "CUSTOM SCOREBOARD");
        this.config.addDefault("Lines.1", " ");
        this.config.addDefault("Lines.2", "&b&l__Team_1:_5");
        this.config.addDefault("Lines.3", " ");
        this.config.addDefault("Lines.4", "&c&l__Team_2:_10");
        this.config.addDefault("Lines.5", " ");
        this.config.addDefault("Lines.6", "&9&l__Timer:_");
        this.config.addDefault("SaveData.start", Boolean.valueOf(false));
        this.config.addDefault("SaveData.stop", Boolean.valueOf(true));
        this.config.addDefault("SaveData.countdown", Boolean.valueOf(true));
        this.config.addDefault("SaveData.show", Boolean.valueOf(true));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(!e.getPlayer().isOp()) return;

        if((e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && e.getHand().equals(EquipmentSlot.HAND)) {
            if(e.getItem().equals(new ItemStack(Material.BLAZE_ROD))) {
                this.config.set("SaveData.stop", true);
                config.set("SaveData.start", false);
            }
            if(e.getItem().equals(new ItemStack(Material.CLOCK))) {
                this.config.set("SaveData.stop", false);
                config.set("SaveData.start", true);
            }
        }
    }
}

