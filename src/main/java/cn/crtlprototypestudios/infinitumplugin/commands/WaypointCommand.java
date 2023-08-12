package cn.crtlprototypestudios.infinitumplugin.commands;

import cn.crtlprototypestudios.infinitumplugin.classes.waypoints.Waypoint;
import cn.crtlprototypestudios.infinitumplugin.managers.FactionsManager;
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
        message.addExtra((ChatColor.YELLOW + String.format(LocalesManager.Locales.getString("msg.command.waypoint.listall.textcomp.in_world_at_pos"),ChatColor.GOLD + waypoint.getWorld().getName())));
        message.addExtra(waypointCoords);

        return message;
    }
    public TextComponent createShareMessage(Player sender, Waypoint waypoint){
        TextComponent message = new TextComponent(ChatColor.AQUA + String.format(LocalesManager.Locales.getString("msg.command.waypoint.share.textcomp.share_waypoint"),ChatColor.GOLD + sender.getName(), ChatColor.GOLD + "\"" + waypoint.getName() + "\""));
        TextComponent addButton = new TextComponent(" " + ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.waypoint.share.textcomp.add_button"));
        addButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/waypoint accept " + sender.getName() + waypoint.getName()));
        addButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(LocalesManager.Locales.getString("msg.command.waypoint.share.textcomp.add_button_tip"))));

        TextComponent denyButton = new TextComponent(" " + ChatColor.RED + LocalesManager.Locales.getString("msg.command.waypoint.share.textcomp.deny_button"));
        denyButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/waypoint accept " + sender.getName() + waypoint.getName()));
        denyButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(LocalesManager.Locales.getString("msg.command.waypoint.share.textcomp.deny_button_tip"))));

        message.addExtra(addButton);
        message.addExtra(denyButton);
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
                        player.sendMessage(ChatColor.RED + String.format(LocalesManager.Locales.getString("msg.command.waypoint.listall.nonexisting_uuid"),String.valueOf(id)));
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
                        player.sendMessage(ChatColor.RED + String.format(LocalesManager.Locales.getString("msg.command.waypoint.waypoint_exists"),(ChatColor.GOLD + "\"" + args[1] + "\"")));
                        return true;
                    }
                    WaypointManager.addWaypoint(player, new Waypoint(args[1], player.getLocation()));
                    player.sendMessage(ChatColor.AQUA + String.format(LocalesManager.Locales.getString("msg.command.waypoint.add.success"),(ChatColor.GOLD + "\"" + args[1] + "\"")));
                    break;
                case "remove":
                    if (!WaypointManager.hasWaypoint(player, args[1])) {
                        player.sendMessage(ChatColor.RED + String.format(LocalesManager.Locales.getString("msg.command.waypoint.null_waypoint"),(ChatColor.GOLD + "\"" + args[1] + "\"")));
                        return true;
                    }
                    WaypointManager.removeWaypoint(player, args[1]);
                    player.sendMessage(ChatColor.AQUA + String.format(LocalesManager.Locales.getString("msg.command.waypoint.remove.success"),(ChatColor.GOLD + "\"" + args[1] + "\"")));
                    break;
                case "goto":
                    if (!WaypointManager.hasWaypoint(player, args[1])) {
                        player.sendMessage(ChatColor.RED + String.format(LocalesManager.Locales.getString("msg.command.waypoint.null_waypoint"),(ChatColor.GOLD + "\"" + args[1] + "\"")));
                        return true;
                    }
                    player.teleport(WaypointManager.getWaypoint(player, args[1]).toLocation());
                    player.sendMessage(String.format(LocalesManager.Locales.getString("msg.command.waypoint.goto.success"),(ChatColor.GOLD + "\"" + args[1] + "\"")));
                    break;
                case "override":
                    if (!WaypointManager.hasWaypoint(player, args[1])) {
                        player.sendMessage(ChatColor.RED + String.format(LocalesManager.Locales.getString("msg.command.waypoint.null_waypoint"),(ChatColor.GOLD + "\"" + args[1] + "\"")));
                        return true;
                    }

                    player.sendMessage(String.format(LocalesManager.Locales.getString("msg.command.waypoint.override.success"),(ChatColor.GOLD + "\"" + args[1] + "\"")));
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
                            player.sendMessage(ChatColor.RED + String.format(LocalesManager.Locales.getString("msg.command.waypoint.listall.nonexisting_uuid"),String.valueOf(id)));
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
                    player.sendMessage(ChatColor.GRAY + (!player.isOp() ? LocalesManager.Locales.getString("msg.command.waypoint.usage") : LocalesManager.Locales.getString("msg.command.waypoint.usage_op")));
                    break;
                case "accept":
                    player.sendMessage(ChatColor.GRAY + (!player.isOp() ? LocalesManager.Locales.getString("msg.command.waypoint.usage") : LocalesManager.Locales.getString("msg.command.waypoint.usage_op")));
                    break;
                default:
                    player.sendMessage(ChatColor.GRAY + (!player.isOp() ? LocalesManager.Locales.getString("msg.command.waypoint.usage") : LocalesManager.Locales.getString("msg.command.waypoint.usage_op")));
                    break;
            }
            return true;
        } else if (args.length == 3) {
            if(args[0].equalsIgnoreCase("share")){
                if(args[2].equalsIgnoreCase("faction")){
                    //TODO: composite clickable message
                }
            }
            if(args[0].equalsIgnoreCase("accept")){
                if(!WaypointManager.sharedWaypoints.keySet().contains(player.getUniqueId())){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.waypoint.accept.no_shared_waypoints"));
                    return true;
                }
                if(WaypointManager.sharedWaypoints.get(player.getUniqueId()).isEmpty()){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.waypoint.accept.no_shared_waypoints"));
                    return true;
                }
                if(Bukkit.getPlayer(args[1]) == null){
                    player.sendMessage(ChatColor.RED + String.format(LocalesManager.Locales.getString("msg.command.waypoint.accept.nonexisting_player"),(ChatColor.GOLD + "\"" + args[1] + "\"")));
                    return true;
                }
                if(WaypointManager.sharedWaypoints.get(player.getUniqueId()).get(Bukkit.getPlayer(args[1])) == null || WaypointManager.sharedWaypoints.get(player.getUniqueId()).get(Bukkit.getPlayer(args[1])).isEmpty()){
                    player.sendMessage(ChatColor.RED + String.format(LocalesManager.Locales.getString("msg.command.waypoint.accept.nonexisting_player"),(ChatColor.GOLD + "\"" + args[1] + "\"")));
                    return true;
                }
                if(WaypointManager.hasWaypoint(player,WaypointManager.getWaypoint(Bukkit.getPlayer(args[1]), args[2]).getName())){
                    player.sendMessage(ChatColor.RED + String.format(LocalesManager.Locales.getString("msg.command.waypoint.accept.existing_waypoint"),(ChatColor.GOLD + "\"" + args[2] + "\"")));
                    return true;
                }
                boolean flag = false;
                for(Waypoint wp : WaypointManager.sharedWaypoints.get(player.getUniqueId()).get(Bukkit.getPlayer(args[1]))){
                    if(WaypointManager.getWaypoint(Bukkit.getPlayer(args[1]), args[2]) != null && WaypointManager.getWaypoint(Bukkit.getPlayer(args[1]), args[2]).equals(wp)){
                        WaypointManager.addWaypoint(player, wp);
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    player.sendMessage(ChatColor.RED + String.format(LocalesManager.Locales.getString("msg.command.waypoint.accept.nonexisting_waypoint"),(ChatColor.GOLD + "\"" + args[2] + "\"")));
                }
                return true;
            }
        } else if (args.length >= 4) {
            if(args[0].equalsIgnoreCase("share")){
                if(args[2].equalsIgnoreCase("with")){
                    for(int i = 3; i < args.length; i++){
                        Player target = Bukkit.getPlayer(args[i]);
                        if(target == null) {
                            player.sendMessage(ChatColor.RED + String.format(LocalesManager.Locales.getString("msg.command.waypoint.share.nonexisting_player"),(ChatColor.GOLD + "\"" + args[i] + "\"")));
                            continue;
                        }else{
                            if(!WaypointManager.hasWaypoint(player, args[1])){
                                player.sendMessage(ChatColor.RED + String.format(LocalesManager.Locales.getString("msg.command.waypoint.null_waypoint"),(ChatColor.GOLD + "\"" + args[1] + "\"")));
                                return true;
                            }
                            WaypointManager.shareWaypoint(player, target, args[1]);
                            player.sendMessage(String.format(LocalesManager.Locales.getString("msg.command.waypoint.share.success"),(ChatColor.GOLD + "\"" + args[1] + "\""), ChatColor.GOLD + target.getName()));
                            target.sendMessage(createShareMessage(player, WaypointManager.getWaypoint(player, args[1])));
                        }
                    }
                }
            }
        } else {
            player.sendMessage(ChatColor.GRAY + (!player.isOp() ? LocalesManager.Locales.getString("msg.command.waypoint.usage") : LocalesManager.Locales.getString("msg.command.waypoint.usage_op")));
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
            suggestions.add("share");
            suggestions.add("list");
            suggestions.add("override");
            suggestions.add("accept");
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
        } else if (args.length == 2 && args[0].equalsIgnoreCase("accept")) {
            List<String> suggestions = new ArrayList<>();
            for (UUID p : WaypointManager.sharedWaypoints.get(player.getUniqueId()).keySet()) {
                if (Bukkit.getPlayer(p) == null) continue;
                suggestions.add(Bukkit.getPlayer(p).getName());
            }
            return suggestions;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("accept")) {
            List<String> suggestions = new ArrayList<>();
            if(WaypointManager.sharedWaypoints.get(player.getUniqueId()).get(Bukkit.getPlayer(args[1])) == null || WaypointManager.sharedWaypoints.get(player.getUniqueId()).get(Bukkit.getPlayer(args[1])).isEmpty()) return null;
            for (Waypoint wp : WaypointManager.sharedWaypoints.get(player.getUniqueId()).get(Bukkit.getPlayer(args[1]))) {
                if(Bukkit.getPlayer(args[1]) == null) continue;
                if(WaypointManager.getWaypoint(Bukkit.getPlayer(args[1]), args[2]) == null) continue;
                suggestions.add(wp.getName());
            }
            return suggestions;

        } else if (args.length == 3 && args[0].equalsIgnoreCase("share")){
            List<String> suggestions = new ArrayList<>();
            suggestions.add("everyone");
            suggestions.add("with");
            suggestions.add("faction");
            return suggestions;
        } else if (args.length >= 4 && args[0].equalsIgnoreCase("share")) {
            List<String> suggestions = new ArrayList<>();
            if (args[2].equalsIgnoreCase("with")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().startsWith(args[3])) {
                        suggestions.add(p.getName());
                    }
                }
            }
        } else {
            // No suggestions for other arguments
            return null;
        }
        return null;
    }
}
