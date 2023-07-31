package cn.crtlprototypestudios.infminecrafthelloplugin.commands;

import cn.crtlprototypestudios.infminecrafthelloplugin.managers.DeathManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BackCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!command.getName().equalsIgnoreCase("back")) return true;

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) commandSender;
        if(DeathManager.getDeathLocation(player) != null){
            player.teleport(DeathManager.getDeathLocation(player));
            player.sendMessage(ChatColor.AQUA + "You have been teleported to your previous Death Position.");
        }else{
            player.sendMessage(ChatColor.RED + "You do not have any recorded Death Positions.");
        }
        DeathManager.clearDeathLocation(player);
        return true;
    }
}
