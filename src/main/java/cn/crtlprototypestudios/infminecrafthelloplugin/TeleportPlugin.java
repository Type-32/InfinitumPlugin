package cn.crtlprototypestudios.infminecrafthelloplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class TeleportPlugin extends JavaPlugin {

    private HashMap<UUID, UUID> tpaRequests = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if(command.getName().equalsIgnoreCase("tpa")) {
            if(args.length != 1) {
                player.sendMessage(ChatColor.RED + "Usage: /tpa <player>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if(target == null) {
                player.sendMessage(ChatColor.RED + "That player is not online!");
                return true;
            }

            if(target.equals(player)) {
                player.sendMessage(ChatColor.RED + "You cannot teleport to yourself!");
                return true;
            }

            tpaRequests.put(target.getUniqueId(), player.getUniqueId());
            target.sendMessage(ChatColor.AQUA + player.getName() + " has requested to teleport to you.");
            target.sendMessage(ChatColor.AQUA + "To accept, use /tpaccept");
            player.sendMessage(ChatColor.AQUA + "Teleport request sent to " + target.getName() + ". Waiting for response...");
            return true;

        } else if(command.getName().equalsIgnoreCase("tpaccept")) {
            if(!tpaRequests.containsKey(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You have no pending requests!");
                return true;
            }

            Player requester = Bukkit.getPlayer(tpaRequests.get(player.getUniqueId()));

            if(requester == null) {
                player.sendMessage(ChatColor.RED + "That player is no longer online!");
                tpaRequests.remove(player.getUniqueId());
                return true;
            }

            requester.teleport(player);
            tpaRequests.remove(player.getUniqueId());

            player.sendMessage(ChatColor.AQUA + "Accepted teleport request from " + requester.getName());
            requester.sendMessage(ChatColor.AQUA + player.getName() + " accepted your teleport request!");

            return true;

        } else if(command.getName().equalsIgnoreCase("home")) {
            if(!player.hasPermission("teleport.home")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                return true;
            }

            Location home = HomeManager.getHome(player); // Custom home manager class

            if(home == null) {
                player.sendMessage(ChatColor.RED + "You have not set a home!");
                return true;
            }

            player.teleport(home);
            player.sendMessage(ChatColor.AQUA + "Teleported to your home!");
            return true;

        } else if(command.getName().equalsIgnoreCase("spawn")) {
            if(!player.hasPermission("teleport.spawn")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                return true;
            }

            player.teleport(player.getWorld().getSpawnLocation());
            player.sendMessage(ChatColor.AQUA + "Teleported to the world spawn!");
            return true;

        } else if(command.getName().equalsIgnoreCase("back")) {
            if(!player.hasPermission("teleport.back")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                return true;
            }

            Location back = BackManager.getBackLocation(player); // Custom back manager class

            if(back == null) {
                player.sendMessage(ChatColor.RED + "You have no previous location to return to!");
                return true;
            }

            player.teleport(back);
            player.sendMessage(ChatColor.AQUA + "Returned to your previous location!");
            return true;
        }

        return false;
    }

}