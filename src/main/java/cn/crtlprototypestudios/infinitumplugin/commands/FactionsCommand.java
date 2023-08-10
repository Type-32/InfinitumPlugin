package cn.crtlprototypestudios.infinitumplugin.commands;

import cn.crtlprototypestudios.infinitumplugin.classes.factions.Faction;
import cn.crtlprototypestudios.infinitumplugin.classes.factions.FactionPlayerInfo;
import cn.crtlprototypestudios.infinitumplugin.classes.factions.FactionSettings;
import cn.crtlprototypestudios.infinitumplugin.managers.FactionsManager;
import cn.crtlprototypestudios.infinitumplugin.managers.LocalesManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FactionsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) commandSender;
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.player_exclusive"));
            return true;
        }

        if(args.length <= 1 && args[0].equalsIgnoreCase("create")){
            player.sendMessage(ChatColor.GRAY + LocalesManager.Locales.getString("msg.command.factions.usage"));
            return true;
        }

        if(args.length > 1){

        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            // Provide suggestions for the first argument ("/waypoint <arg>")
            if(!FactionsManager.findPlayerInFaction(player)) {
                suggestions.add("create");
                suggestions.add("join");
            }else{
                if(FactionsManager.isPlayerLeader(player) || FactionsManager.isPlayerModerator(player)){
                    suggestions.add("invite");
                    suggestions.add("kick");
                }
                if (FactionsManager.isPlayerLeader(player)) {
                    suggestions.add("promote");
                    suggestions.add("demote");
                    suggestions.add("disband");
                    suggestions.add("rules");
                    suggestions.add("prefix");
                    suggestions.add("suffix");
                    suggestions.add("color");
                }
                suggestions.add("info");
                suggestions.add("leave");
            }

            suggestions.add("list");
            if(player.isOp()){
                suggestions.add("listall");
            }
            return suggestions;
        } else if (args.length == 2) {
            if(args[0].equalsIgnoreCase("promote")) {
                Faction f = FactionsManager.getPlayerFaction(player);
                for (FactionPlayerInfo p : f.getMembers()) {
                    suggestions.add(p.getUsername());
                }
                return suggestions;
            }else if (args[0].equalsIgnoreCase("demote")){
                Faction f = FactionsManager.getPlayerFaction(player);
                for (FactionPlayerInfo p : f.getModerators()) {
                    suggestions.add(p.getUsername());
                }
                return suggestions;
            }else if (args[0].equalsIgnoreCase("kick")){
                FactionPlayerInfo f = FactionsManager.getPlayerInFaction(player);
                if(f.isModerator()){
                    Faction faction = FactionsManager.getPlayerFaction(player);
                    for (FactionPlayerInfo p : faction.getMembers()) {
                        suggestions.add(p.getUsername());
                    }
                }else if(f.isLeader()){
                    Faction faction = FactionsManager.getPlayerFaction(player);
                    for (FactionPlayerInfo p : faction.getAllMembers(true)) {
                        if(!p.isLeader()){
                            suggestions.add(p.getUsername());
                        }
                    }
                }
                return suggestions;
            }else if (args[0].equalsIgnoreCase("color")){
                for(ChatColor c : ChatColor.values()){
                    suggestions.add(c.name());
                }
                return suggestions;
            }else if (args[0].equalsIgnoreCase("prefix") || args[0].equalsIgnoreCase("suffix")){
                suggestions.clear();
                return suggestions;
            }else if (args[0].equalsIgnoreCase("invite")) {
                Faction faction = FactionsManager.getPlayerFaction(player);
                for (Player p : player.getServer().getOnlinePlayers()) {
                    if(!FactionsManager.isPlayerLeader(p)) suggestions.add(p.getName());
                }
                return suggestions;
            }else if (args[0].equalsIgnoreCase("join")) {
                for (Faction f : FactionsManager.getFactions()) {
                    if(f.factionSettings.factionPublic) suggestions.add(f.getName());
                }
                return suggestions;
            }else if (args[0].equalsIgnoreCase("rules")){
                if(FactionsManager.isPlayerLeader(player)){
                    suggestions.addAll(FactionSettings.getRulesKey());
                }
            }
        } else if (args.length == 3){

        } else {
            // No suggestions for other arguments
            return null;
        }
        return suggestions;
    }
}
