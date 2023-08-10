package cn.crtlprototypestudios.infinitumplugin.commands;

import cn.crtlprototypestudios.infinitumplugin.managers.LocalesManager;
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

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.player_exclusive"));
            return true;
        }

        Player player = (Player) commandSender;


        if (getOverworldSpawnLocation() != null){
            player.teleport(getOverworldSpawnLocation());
            player.sendMessage(ChatColor.AQUA + LocalesManager.Locales.getString("msg.command.spawn.success"));
        } else {
            player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.spawn.failure"));
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
