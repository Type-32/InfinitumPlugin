package cn.crtlprototypestudios.infminecrafthelloplugin.commands;

import cn.crtlprototypestudios.infminecrafthelloplugin.classes.Waypoint;
import cn.crtlprototypestudios.infminecrafthelloplugin.managers.WaypointManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
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
import java.util.Objects;
import java.util.UUID;

public class WaypointCommand implements CommandExecutor, TabCompleter {

    public TextComponent createListMessage(Waypoint waypoint) {
        TextComponent message = new TextComponent(ChatColor.YELLOW + " -> ");

        TextComponent waypointName = new TextComponent(ChatColor.GOLD + waypoint.getName());
        waypointName.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/waypoint goto " + waypoint.getName()));
        waypointName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Click to Teleport to Waypoint")));

        TextComponent waypointCoords = new TextComponent(ChatColor.GOLD + waypoint.posToString(true));
        waypointCoords.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, waypoint.posToString(true)));
        waypointCoords.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Click to Copy Coordinates to Clipboard")));

        message.addExtra(waypointName);
        message.addExtra((ChatColor.YELLOW + " in ") + (ChatColor.GOLD + waypoint.getWorld().getName()) + (ChatColor.YELLOW + " at "));
        message.addExtra(waypointCoords);

        return message;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) commandSender;

        if (args.length == 0){
            player.sendMessage(ChatColor.GRAY + "Usage: /waypoint <goto|add|remove|list|override> <Waypoint Name>");
            return true;
        }

        if (args.length == 1) {
            if(args[0].equals("list")) {
                if(WaypointManager.getWaypointList(player).isEmpty() || WaypointManager.getWaypointList(player) == null){
                    player.sendMessage((ChatColor.RED + "You don't have any waypoints! Use ") + (ChatColor.AQUA + "/waypoint add <Waypoint Name>") + (ChatColor.RED + " to create more waypoints."));
                    return true;
                }
                player.sendMessage(ChatColor.AQUA + "You currently have the following waypoints:");
                for(Waypoint wp : WaypointManager.getWaypointList(player).getValues()){
                    player.sendMessage(createListMessage(wp));
                }
                return true;
            }
            if(args[0].equals("listall")){
                if(!player.isOp()){
                    player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                    return true;
                }
                if(WaypointManager.getPlayers().isEmpty()){
                    player.sendMessage(ChatColor.RED + "There are no available waypoints!");
                    return true;
                }
                player.sendMessage(ChatColor.AQUA + "All Available Waypoints:");
                for(UUID id : WaypointManager.getPlayers()){
                    if (Bukkit.getPlayer(id) == null) {
                        player.sendMessage(ChatColor.RED + "PlayerUUID " + id + " does not exist. Skipping This List.");
                        continue;
                    }
                    player.sendMessage(ChatColor.GOLD + Bukkit.getPlayer(id).getName());
                    for(Waypoint wp : WaypointManager.getWaypointList(Bukkit.getPlayer(id)).getValues()){
                        player.sendMessage(createListMessage(wp));
                    }
                }
                return true;
            }
            player.sendMessage(ChatColor.GRAY + "Usage: /waypoint <goto|add|remove|list|override> <Waypoint Name>");
            return true;
        }else if(args.length == 2){
            if(args[0].equals("list")){
                if(WaypointManager.getWaypointList(player).isEmpty() || WaypointManager.getWaypointList(player) == null){
                    player.sendMessage((ChatColor.RED + "You don't have any waypoints! Use ") + (ChatColor.AQUA + "/waypoint add <Waypoint Name>") + (ChatColor.RED + " to create more waypoints."));
                    return true;
                }
                player.sendMessage(ChatColor.AQUA + "You currently have the following waypoints:");
                for(Waypoint wp : WaypointManager.getWaypointList(player).getValues()){
                    player.sendMessage(createListMessage(wp));
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
                case "listall":
                    if(!player.isOp()){
                        player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                        return true;
                    }
                    if(WaypointManager.getPlayers().isEmpty()){
                        player.sendMessage(ChatColor.RED + "There are no available waypoints!");
                        return true;
                    }
                    player.sendMessage(ChatColor.AQUA + "All Available Waypoints:");
                    for(UUID id : WaypointManager.getPlayers()){
                        player.sendMessage(ChatColor.GOLD + player.getName());
                        for(Waypoint wp : WaypointManager.getWaypointList(player).getValues()){
                            player.sendMessage(createListMessage(wp));
                        }
                    }
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
}
