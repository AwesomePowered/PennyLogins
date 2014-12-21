package net.awesomepowered.pennylogins;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by John on 12/20/2014.
 */
public class PennyLogins extends JavaPlugin implements Listener {

    public int curLogins;

    String[] message = {
            "&6-x-",
            "&aFrom December 20th 2014 to January 2nd 2015",
            "&aEvery login you make will count as a penny donated",
            "&ato either water.org or doctorswithoutborders.org",
            "&aWe currently have &4%PENNIES &aof maximum &c10000 &alogins.",
            "&aNew player counts as 2 pennies, normal login counts as 1",
            "&6-x-"
    };

    public void onEnable() {
        saveDefaultConfig();
        curLogins = getConfig().getInt("CurrentLogins");
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {
        getConfig().set("CurrentLogins", curLogins);
        saveConfig();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent ev) {
        Player p = ev.getPlayer();
        if (p.hasPlayedBefore()) {
            doTell(p, 1);
        } else {
            doTell(p, 2);
        }
    }

    public void doTell(final Player p, final int i) {
        new BukkitRunnable() {
            @Override
            public void run() {
                p.sendMessage(ChatColor.GOLD + "-x-");
                p.sendMessage(ChatColor.GREEN + "We\'ll be donating " + ChatColor.RED + addPenny(i) + ChatColor.GREEN + " pennies to charity.");
                p.sendMessage(ChatColor.GREEN + "Say " + ChatColor.GOLD + "?pennylogins" + ChatColor.GREEN + " for more information.");
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
                p.sendMessage(ChatColor.GOLD + "-x-");
            }
        }.runTaskLater(this, 20);
    }

    public int addPenny(int i) {
        curLogins = curLogins + i;
        getConfig().set("CurrentLogins", curLogins);
        saveConfig();
        return curLogins;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent ev) {
        if (ev.getMessage().equalsIgnoreCase("?pennylogins")) {
            ev.setCancelled(true);
            for (String x : message) {
                ev.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', x.replace("%PENNIES", String.valueOf(curLogins))));
            }
        }
    }
}
