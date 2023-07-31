package cn.crtlprototypestudios.infminecrafthelloplugin.commands;

import cn.crtlprototypestudios.infminecrafthelloplugin.InfMinecraftHelloPlugin;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WaypointCommand implements CommandExecutor, TabCompleter {
    private HashMap<String, Location> playerWaypoints = new HashMap<>();
    private HashMap<String, String> playerWPDimensions = new HashMap<>();
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
                if(playerWaypoints.isEmpty()){
                    player.sendMessage((ChatColor.RED + "You don't have any waypoints! Use ") + (ChatColor.AQUA + "/waypoint add <Waypoint Name>") + (ChatColor.RED + " to create more waypoints."));
                    return true;
                }
                player.sendMessage(ChatColor.AQUA + "You currently have the following waypoints:");
                for(String wps : playerWaypoints.keySet()){
                    player.sendMessage((ChatColor.GOLD + wps) + (ChatColor.YELLOW + " at Position " + String.valueOf(playerWaypoints.get(wps).getBlockX()) + " " + String.valueOf(playerWaypoints.get(wps).getBlockY()) + " " + String.valueOf(playerWaypoints.get(wps).getBlockZ()) + " " + "at Dimension " + playerWPDimensions.get(wps)));
                }
            }
        }else if(args.length == 2){
            if(args[0].equals("list")) {
                player.sendMessage(ChatColor.GRAY + "Usage: /waypoint <goto|add|remove|list|override> <Waypoint Name>");
                return true;
            } else if (args[0].equals("add")){
                if(playerWaypoints.containsKey(args[1])){
                    player.sendMessage((ChatColor.RED + "The Waypoint with the name ") + (ChatColor.GOLD + "\"" + args[1] + "\" ") + (ChatColor.RED + "already exists! If you wish to override the position of the waypoint, please use ") + (ChatColor.GOLD + "/waypoint override <Waypoint Name>."));
                    return true;
                }
                playerWaypoints.put(args[1],player.getLocation());
                playerWPDimensions.put(args[1],checkPlayerDimension(player));
                player.sendMessage((ChatColor.AQUA + "You have added Waypoint ") + (ChatColor.GOLD + "\"" + args[1] + "\"."));
                return true;
            } else if (args[0].equals("remove")) {
                if(!playerWaypoints.containsKey(args[1])){
                    player.sendMessage((ChatColor.RED + "The Waypoint with the name ") + (ChatColor.GOLD + "\"" + args[1] + "\" ") + (ChatColor.RED + "does not exist!"));
                    return true;
                }
                playerWaypoints.remove(args[1]);
                playerWPDimensions.remove(args[1]);
                player.sendMessage((ChatColor.AQUA + "You have removed Waypoint ") + (ChatColor.GOLD + "\"" + args[1] + "\"."));
                return true;
            } else if (args[0].equals("goto")) {
                if(!playerWaypoints.containsKey(args[1])){
                    player.sendMessage((ChatColor.RED + "The Waypoint with the name ") + (ChatColor.GOLD + "\"" + args[1] + "\" ") + (ChatColor.RED + "does not exist!"));
                    return true;
                }
                player.teleport(playerWaypoints.get(args[1]));
                player.sendMessage((ChatColor.AQUA + "You have teleported to Waypoint ") + (ChatColor.GOLD + "\"" + args[1] + "\"."));
                return true;
            } else if (args[0].equals("override")) {
                if(!playerWaypoints.containsKey(args[1])){
                    player.sendMessage((ChatColor.RED + "The Waypoint with the name ") + (ChatColor.GOLD + "\"" + args[1] + "\" ") + (ChatColor.RED + "does not exist!"));
                    return true;
                }

                player.sendMessage((ChatColor.AQUA + "You have overrided Waypoint ") + (ChatColor.GOLD + "\"" + args[1] + "\"") + (ChatColor.AQUA + " from ") + (ChatColor.GOLD + String.valueOf(playerWaypoints.get(args[1]).getBlockX() + " " + String.valueOf(playerWaypoints.get(args[1]).getBlockY() + " " + String.valueOf(playerWaypoints.get(args[1]).getBlockZ() + " at Dimension ") + playerWPDimensions.get(args[1])) + (ChatColor.AQUA + " to ") + (ChatColor.GOLD + String.valueOf(player.getLocation().getBlockX()) + " " + String.valueOf(player.getLocation().getBlockY()) + " " + String.valueOf(player.getLocation().getBlockZ()) + " at Dimension " + checkPlayerDimension(player)))));
                playerWaypoints.put(args[1],player.getLocation());
                playerWPDimensions.put(args[1],checkPlayerDimension(player));
                return true;
            } else {
                player.sendMessage(ChatColor.GRAY + "Usage: /waypoint <goto|add|remove|list|override> <Waypoint Name>");
                return true;
            }
        } else {
            player.sendMessage(ChatColor.GRAY + "Usage: /waypoint <goto|add|remove|list|override> <Waypoint Name>");
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            // Provide suggestions for the first argument ("/waypoint <arg>")
            List<String> suggestions = new ArrayList<>();
            suggestions.add("goto");
            suggestions.add("add");
            suggestions.add("remove");
            suggestions.add("list");
            suggestions.add("override");
            return suggestions;
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("goto") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("override"))) {
            List<String> suggestions = new ArrayList<>(playerWaypoints.keySet());
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
