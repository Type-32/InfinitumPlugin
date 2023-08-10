package cn.crtlprototypestudios.infinitumplugin.commands;

import cn.crtlprototypestudios.infinitumplugin.classes.waypoints.Waypoint;
import cn.crtlprototypestudios.infinitumplugin.managers.LocalesManager;
import cn.crtlprototypestudios.infinitumplugin.managers.WaypointManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class WaypointCommand implements CommandExecutor, TabCompleter {

    public TextComponent createListMessage(Waypoint waypoint) {
        TextComponent message = new TextComponent(ChatColor.YELLOW + " -> ");

        TextComponent waypointName = new TextComponent(ChatColor.GOLD + waypoint.getName());
        waypointName.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/waypoint goto " + waypoint.getName()));
        waypointName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(LocalesManager.Locales.getString("msg.command.waypoint.listall.textcomp.click_tp_tip"))));

        TextComponent waypointCoords = new TextComponent(ChatColor.GOLD + waypoint.posToString(true));
        waypointCoords.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, waypoint.posToString(true)));
        waypointCoords.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(LocalesManager.Locales.getString("msg.command.waypoint.listall.textcomp.click_copy_coords"))));

        message.addExtra(waypointName); //(ChatColor.GOLD + waypoint.getWorld().getName())
        message.addExtra((ChatColor.YELLOW + LocalesManager.Locales.getString("msg.command.waypoint.listall.textcomp.in_world_at_pos").format(ChatColor.GOLD + waypoint.getWorld().getName())));
        message.addExtra(waypointCoords);

        return message;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.player_exclusive"));
            return true;
        }

        Player player = (Player) commandSender;

        if (args.length == 0){
            player.sendMessage(ChatColor.GRAY + (!player.isOp() ? LocalesManager.Locales.getString("msg.command.waypoint.usage") : LocalesManager.Locales.getString("msg.command.waypoint.usage_op")));
            return true;
        }

        if (args.length == 1) {
            if(args[0].equals("list")) {
                if(WaypointManager.getWaypointList(player).isEmpty() || WaypointManager.getWaypointList(player) == null){
                    player.sendMessage((ChatColor.RED + LocalesManager.Locales.getString("msg.command.waypoint.list.no_waypoints")));
                    return true;
                }
                player.sendMessage(ChatColor.AQUA + LocalesManager.Locales.getString("msg.command.waypoint.list.current_waypoints_header"));
                for(Waypoint wp : WaypointManager.getWaypointList(player).getValues()){
                    player.sendMessage(createListMessage(wp));
                }
                return true;
            }
            if(args[0].equals("listall")){
                if(!player.isOp()){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.used_without_op_permission"));
                    return true;
                }
                if(WaypointManager.getPlayers().isEmpty()){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.waypoint.listall.no_waypoints"));
                    return true;
                }
                player.sendMessage(ChatColor.AQUA + LocalesManager.Locales.getString("msg.command.waypoint.listall.available_waypoints_header"));
                for(UUID id : WaypointManager.getPlayers()){
                    if (Bukkit.getPlayer(id) == null) {
                        player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.waypoint.listall.nonexisting_uuid").format(String.valueOf(id)));
                        WaypointManager.removePlayer(id);
                        continue;
                    }
                    player.sendMessage(ChatColor.GOLD + Bukkit.getPlayer(id).getName());
                    for(Waypoint wp : WaypointManager.getWaypointList(Bukkit.getPlayer(id)).getValues()){
                        player.sendMessage(createListMessage(wp));
                    }
                }
                return true;
            }
            player.sendMessage(ChatColor.GRAY + (!player.isOp() ? LocalesManager.Locales.getString("msg.command.waypoint.usage") : LocalesManager.Locales.getString("msg.command.waypoint.usage_op")));
            return true;
        }else if(args.length == 2){
            if(args[0].equals("list")) {
                if(WaypointManager.getWaypointList(player).isEmpty() || WaypointManager.getWaypointList(player) == null){
                    player.sendMessage((ChatColor.RED + LocalesManager.Locales.getString("msg.command.waypoint.list.no_waypoints")));
                    return true;
                }
                player.sendMessage(ChatColor.AQUA + LocalesManager.Locales.getString("msg.command.waypoint.list.current_waypoints_header"));
                for(Waypoint wp : WaypointManager.getWaypointList(player).getValues()){
                    player.sendMessage(createListMessage(wp));
                }
                return true;
            }

            switch (args[0]) {
                case "add":
                    if (WaypointManager.hasWaypoint(player, args[1])) {
                        player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.waypoint.waypoint_exists").format(ChatColor.GOLD + "\"" + args[1] + "\""));
                        return true;
                    }
                    WaypointManager.addWaypoint(player, new Waypoint(args[1], player.getLocation()));
                    player.sendMessage(ChatColor.AQUA + LocalesManager.Locales.getString("msg.command.waypoint.add.success").format(ChatColor.GOLD + "\"" + args[1] + "\""));
                    break;
                case "remove":
                    if (!WaypointManager.hasWaypoint(player, args[1])) {
                        player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.waypoint.null_waypoint").format(ChatColor.GOLD + "\"" + args[1] + "\""));
                        return true;
                    }
                    WaypointManager.removeWaypoint(player, args[1]);
                    player.sendMessage(ChatColor.AQUA + LocalesManager.Locales.getString("msg.command.waypoint.remove.success").format(ChatColor.GOLD + "\"" + args[1] + "\""));
                    break;
                case "goto":
                    if (!WaypointManager.hasWaypoint(player, args[1])) {
                        player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.waypoint.null_waypoint").format(ChatColor.GOLD + "\"" + args[1] + "\""));
                        return true;
                    }
                    player.teleport(WaypointManager.getWaypoint(player, args[1]).toLocation());
                    player.sendMessage(LocalesManager.Locales.getString("msg.command.waypoint.goto.success").format(ChatColor.GOLD + "\"" + args[1] + "\""));
                    break;
                case "override":
                    if (!WaypointManager.hasWaypoint(player, args[1])) {
                        player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.waypoint.null_waypoint").format(ChatColor.GOLD + "\"" + args[1] + "\""));
                        return true;
                    }

                    player.sendMessage(LocalesManager.Locales.getString("msg.command.waypoint.override.success").format(ChatColor.GOLD + "\"" + args[1] + "\""));
                    WaypointManager.overrideWaypoint(player, args[1], new Waypoint(args[1],player.getLocation()));
                    break;
                case "listall":
                    if(!player.isOp()){
                        player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.used_without_op_permission"));
                        return true;
                    }
                    if(WaypointManager.getPlayers().isEmpty()){
                        player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.waypoint.listall.no_waypoints"));
                        return true;
                    }
                    player.sendMessage(ChatColor.AQUA + LocalesManager.Locales.getString("msg.command.waypoint.listall.available_waypoints_header"));
                    for(UUID id : WaypointManager.getPlayers()){
                        if (Bukkit.getPlayer(id) == null) {
                            player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.waypoint.listall.nonexisting_uuid").format(String.valueOf(id)));
                            WaypointManager.removePlayer(id);
                            continue;
                        }
                        player.sendMessage(ChatColor.GOLD + Bukkit.getPlayer(id).getName());
                        for(Waypoint wp : WaypointManager.getWaypointList(Bukkit.getPlayer(id)).getValues()){
                            player.sendMessage(createListMessage(wp));
                        }
                    }
                    break;
                case "share":

                    break;
                default:
                    player.sendMessage(ChatColor.GRAY + (!player.isOp() ? LocalesManager.Locales.getString("msg.command.waypoint.usage") : LocalesManager.Locales.getString("msg.command.waypoint.usage_op")));
                    break;
            }
            return true;
        } else {
            player.sendMessage(ChatColor.GRAY + (!player.isOp() ? LocalesManager.Locales.getString("msg.command.waypoint.usage") : LocalesManager.Locales.getString("msg.command.waypoint.usage_op")));
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
            suggestions.add("share");
            suggestions.add("list");
            suggestions.add("override");
            if(player.isOp()){
                suggestions.add("listall");
            }
            return suggestions;
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("goto") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("override"))) {
            List<String> suggestions = new ArrayList<>();
            for (Waypoint wp : WaypointManager.getWaypointList(player).getValues()) {
                suggestions.add(wp.getName());
            }
            return suggestions;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("share")){
            List<String> suggestions = new ArrayList<>();
            for (Waypoint wp : WaypointManager.getWaypointList(player).getValues()) {
                suggestions.add(wp.getName());
            }
            return suggestions;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("share")){
            List<String> suggestions = new ArrayList<>();
            suggestions.add("everyone");
            suggestions.add("with");
            suggestions.add("faction");
            return suggestions;
        } else {
            // No suggestions for other arguments
            return null;
        }
    }
}
