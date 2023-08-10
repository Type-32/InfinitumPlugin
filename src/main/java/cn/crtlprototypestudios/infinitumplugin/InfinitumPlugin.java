package cn.crtlprototypestudios.infinitumplugin;

import cn.crtlprototypestudios.infinitumplugin.commands.*;
import cn.crtlprototypestudios.infinitumplugin.listeners.PlayerEventListener;
import cn.crtlprototypestudios.infinitumplugin.managers.WaypointManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class InfinitumPlugin extends JavaPlugin {

    private static InfinitumPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);

        //Initialize locales

        // Register your commands and event listeners here
        WaypointCommand waypointCommand = new WaypointCommand();
        RulesCommand rulesCommand = new RulesCommand();
        TpaCommand tpaCommand = new TpaCommand();
        FactionsCommand factionsCommand = new FactionsCommand();

        getCommand("tpa").setExecutor(tpaCommand);
        getCommand("tpaccept").setExecutor(tpaCommand);
        getCommand("tpdeny").setExecutor(tpaCommand);

        getCommand("back").setExecutor(new BackCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());

        getCommand("waypoint").setExecutor(waypointCommand);
        getCommand("waypoint").setTabCompleter(waypointCommand);

        getCommand("factions").setExecutor(factionsCommand);
        getCommand("factions").setTabCompleter(factionsCommand);
        //getCommand("rules").setExecutor(rulesCommand);
        //getCommand("rules").setTabCompleter(rulesCommand);

        WaypointManager.readAllWaypoints(this);
    }

    @Override
    public void onDisable(){
        WaypointManager.saveAllWaypoints(this);
    }

    public static InfinitumPlugin getInstance() {
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