package cn.crtlprototypestudios.infinitumplugin;

import cn.crtlprototypestudios.infinitumplugin.commands.*;
import cn.crtlprototypestudios.infinitumplugin.listeners.PlayerEventListener;
import cn.crtlprototypestudios.infinitumplugin.managers.FactionsManager;
import cn.crtlprototypestudios.infinitumplugin.managers.WaypointManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class InfinitumPlugin extends JavaPlugin {

    private static InfinitumPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);

        //Initialize locales

        // Register your commands and event listeners here
        WaypointCommand waypointCommand = new WaypointCommand();
//        RulesCommand rulesCommand = new RulesCommand();
        TpaCommand tpaCommand = new TpaCommand();
        FactionsCommand factionsCommand = new FactionsCommand();

        Objects.requireNonNull(getCommand("tpa")).setExecutor(tpaCommand);
        Objects.requireNonNull(getCommand("tpaccept")).setExecutor(tpaCommand);
        Objects.requireNonNull(getCommand("tpdeny")).setExecutor(tpaCommand);

        Objects.requireNonNull(getCommand("back")).setExecutor(new BackCommand());
        Objects.requireNonNull(getCommand("spawn")).setExecutor(new SpawnCommand());

        Objects.requireNonNull(getCommand("waypoint")).setExecutor(waypointCommand);
        Objects.requireNonNull(getCommand("waypoint")).setTabCompleter(waypointCommand);

        Objects.requireNonNull(getCommand("factions")).setExecutor(factionsCommand);
        Objects.requireNonNull(getCommand("factions")).setTabCompleter(factionsCommand);
        /*
        getCommand("rules").setExecutor(rulesCommand);
        getCommand("rules").setTabCompleter(rulesCommand);
        */

        WaypointManager.readAllWaypoints(this);

        FactionsManager.readFactionsFromFile();
        FactionsManager.readKickQueueFromFile();
    }

    @Override
    public void onDisable() {
        WaypointManager.saveAllWaypoints(this);
    }

    public static InfinitumPlugin getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}