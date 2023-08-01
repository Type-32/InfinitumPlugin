package cn.crtlprototypestudios.infminecrafthelloplugin.commands;

import cn.crtlprototypestudios.infminecrafthelloplugin.classes.Waypoint;
import cn.crtlprototypestudios.infminecrafthelloplugin.managers.WaypointManager;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WaypointCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!command.getName().equalsIgnoreCase("waypoint")) return true;

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) commandSender;

        if (args.length == 0){
            player.sendMessage(ChatColor.GRAY + "Usage: /waypoint <goto|add|remove|list|override> <Waypoint Name>");
            return true;
        }

        if (args.length == 1 ) {
            if(!args[0].equals("list")) {
                player.sendMessage(ChatColor.GRAY + "Usage: /waypoint <goto|add|remove|list|override> <Waypoint Name>");
                return true;
            }else{
                if(WaypointManager.getWaypointList(player).isEmpty()){
                    player.sendMessage((ChatColor.RED + "You don't have any waypoints! Use ") + (ChatColor.AQUA + "/waypoint add <Waypoint Name>") + (ChatColor.RED + " to create more waypoints."));
                    return true;
                }
                player.sendMessage(ChatColor.AQUA + "You currently have the following waypoints:");
                for(Waypoint wp : WaypointManager.getWaypointList(player).getValues()){
                    player.sendMessage((ChatColor.GOLD + wp.getName()) + (ChatColor.YELLOW + " in ") + (ChatColor.GOLD + wp.getWorld().getName()) + (ChatColor.YELLOW + " at ") + (ChatColor.GOLD + wp.posToString()));
                }
            }
        }else if(args.length == 2){
            if(args[0].equals("list")){
                if(WaypointManager.getWaypointList(player).isEmpty()){
                    player.sendMessage((ChatColor.RED + "You don't have any waypoints! Use ") + (ChatColor.AQUA + "/waypoint add <Waypoint Name>") + (ChatColor.RED + " to create more waypoints."));
                    return true;
                }
                player.sendMessage(ChatColor.AQUA + "You currently have the following waypoints:");
                for(Waypoint wp : WaypointManager.getWaypointList(player).getValues()){
                    player.sendMessage((ChatColor.GOLD + wp.getName()) + (ChatColor.YELLOW + " in ") + (ChatColor.GOLD + wp.getWorld().getName()) + (ChatColor.YELLOW + " at ") + (ChatColor.GOLD + wp.posToString()));
                }
            }

            switch (args[0]) {
                case "add":
                    if (WaypointManager.hasWaypoint(player, args[1])) {
                        player.sendMessage((ChatColor.RED + "The Waypoint with the name ") + (ChatColor.GOLD + "\"" + args[1] + "\" ") + (ChatColor.RED + "already exists! If you wish to override the position of the waypoint, please use ") + (ChatColor.GOLD + "/waypoint override <Waypoint Name>."));
                        return true;
                    }
                    WaypointManager.addWaypoint(player, new Waypoint(args[1], player.getLocation()));
                    player.sendMessage((ChatColor.AQUA + "You have added Waypoint ") + (ChatColor.GOLD + "\"" + args[1] + "\"."));
                    break;
                case "remove":
                    if (!WaypointManager.hasWaypoint(player, args[1])) {
                        player.sendMessage((ChatColor.RED + "The Waypoint with the name ") + (ChatColor.GOLD + "\"" + args[1] + "\" ") + (ChatColor.RED + "does not exist!"));
                        return true;
                    }
                    WaypointManager.removeWaypoint(player, args[1]);
                    player.sendMessage((ChatColor.AQUA + "You have removed Waypoint ") + (ChatColor.GOLD + "\"" + args[1] + "\"."));
                    break;
                case "goto":
                    if (!WaypointManager.hasWaypoint(player, args[1])) {
                        player.sendMessage((ChatColor.RED + "The Waypoint with the name ") + (ChatColor.GOLD + "\"" + args[1] + "\" ") + (ChatColor.RED + "does not exist!"));
                        return true;
                    }
                    player.teleport(WaypointManager.getWaypoint(player, args[1]).toLocation());
                    player.sendMessage((ChatColor.AQUA + "You have teleported to Waypoint ") + (ChatColor.GOLD + "\"" + args[1] + "\"."));
                    break;
                case "override":
                    if (!WaypointManager.hasWaypoint(player, args[1])) {
                        player.sendMessage((ChatColor.RED + "The Waypoint with the name ") + (ChatColor.GOLD + "\"" + args[1] + "\" ") + (ChatColor.RED + "does not exist!"));
                        return true;
                    }

                    player.sendMessage((ChatColor.AQUA + "You have overrided Waypoint ") + (ChatColor.GOLD + "\"" + args[1] + "\""));
                    WaypointManager.overrideWaypoint(player, args[1], new Waypoint(args[1],player.getLocation()));
                    break;
                default:
                    player.sendMessage(ChatColor.GRAY + "Usage: /waypoint <goto|add|remove|list|override> <Waypoint Name>");
                    break;
            }
            return true;
        } else {
            player.sendMessage(ChatColor.GRAY + "Usage: /waypoint <goto|add|remove|list|override> <Waypoint Name>");
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            // Provide suggestions for the first argument ("/waypoint <arg>")
            List<String> suggestions = new ArrayList<>();
            suggestions.add("goto");
            suggestions.add("add");
            suggestions.add("remove");
            suggestions.add("list");
            suggestions.add("override");
            if(player.hasPermission("teleport.waypoint.admin")){
                suggestions.add("listall");
            }
            return suggestions;
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("goto") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("override"))) {
            List<String> suggestions = new ArrayList<>();
            for (Waypoint wp : WaypointManager.getWaypointList(player).getValues()) {
                suggestions.add(wp.getName());
            }
            return suggestions;
        } else {
            // No suggestions for other arguments
            return null;
        }
    }

    public String checkPlayerDimension(Player player) {
        World world = player.getWorld();
        String dimensionName;

        if (world.getEnvironment() == World.Environment.NORMAL) {
            dimensionName = "Overworld";
        } else if (world.getEnvironment() == World.Environment.NETHER) {
            dimensionName = "Nether";
        } else if (world.getEnvironment() == World.Environment.THE_END) {
            dimensionName = "End";
        } else {
            // Custom dimension or unknown environment
            dimensionName = "Unknown";
        }

        return dimensionName;
    }
}
