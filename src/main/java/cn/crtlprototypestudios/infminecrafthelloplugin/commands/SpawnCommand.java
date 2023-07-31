package cn.crtlprototypestudios.infminecrafthelloplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!command.getName().equalsIgnoreCase("spawn")) return true;

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) commandSender;


        if (getOverworldSpawnLocation() != null){
            player.teleport(getOverworldSpawnLocation());
            player.sendMessage(ChatColor.AQUA + "You have been teleported to World Spawn.");
        } else {
            player.sendMessage(ChatColor.RED + "Unable to fetch World Spawn from Overworld Saves Data.");
        }
        return true;
    }
    public Location getOverworldSpawnLocation() {
        World overworld = Bukkit.getWorld("world");
        if (overworld != null) {
            return overworld.getSpawnLocation();
        }
        return null; // If the Overworld is not loaded or doesn't exist.
    }
}
