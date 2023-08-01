package cn.crtlprototypestudios.infminecrafthelloplugin;

import cn.crtlprototypestudios.infminecrafthelloplugin.classes.SettingsCategory;
import cn.crtlprototypestudios.infminecrafthelloplugin.commands.*;
import cn.crtlprototypestudios.infminecrafthelloplugin.listeners.PlayerEventListener;
import cn.crtlprototypestudios.infminecrafthelloplugin.managers.DeathManager;
import cn.crtlprototypestudios.infminecrafthelloplugin.managers.WaypointManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;


import java.util.*;

public class InfMinecraftHelloPlugin extends JavaPlugin {

    private static InfMinecraftHelloPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);

        // Register your commands and event listeners here
        WaypointCommand waypointCommand = new WaypointCommand();
        RulesCommand rulesCommand = new RulesCommand();
        TpaCommand tpaCommand = new TpaCommand();
        getCommand("tpa").setExecutor(tpaCommand);
        getCommand("tpaccept").setExecutor(tpaCommand);
        getCommand("tpdeny").setExecutor(tpaCommand);

        getCommand("back").setExecutor(new BackCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());

        getCommand("waypoint").setExecutor(waypointCommand);
        getCommand("waypoint").setTabCompleter(waypointCommand);
        //getCommand("rules").setExecutor(rulesCommand);
        //getCommand("rules").setTabCompleter(rulesCommand);

        WaypointManager.readAllWaypoints(this);
    }

    @Override
    public void onDisable(){
        WaypointManager.saveAllWaypoints(this);
    }

    public static InfMinecraftHelloPlugin getInstance() {
        return instance;
    }

    public static Location getWorldSpawnLocation(World world) {
        return world.getSpawnLocation();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}