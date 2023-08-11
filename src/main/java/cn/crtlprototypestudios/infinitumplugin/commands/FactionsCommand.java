package cn.crtlprototypestudios.infinitumplugin.commands;

import cn.crtlprototypestudios.infinitumplugin.classes.factions.Faction;
import cn.crtlprototypestudios.infinitumplugin.classes.factions.FactionPlayerInfo;
import cn.crtlprototypestudios.infinitumplugin.classes.factions.FactionSettings;
import cn.crtlprototypestudios.infinitumplugin.managers.FactionsManager;
import cn.crtlprototypestudios.infinitumplugin.managers.LocalesManager;
import org.bukkit.Bukkit;
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
import java.util.UUID;

public class FactionsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) commandSender;
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.player_exclusive"));
            return true;
        }

        if((args.length <= 1 || args.length == 0) && !(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("listall") || args[0].equalsIgnoreCase("info"))){
            player.sendMessage(ChatColor.GRAY + LocalesManager.Locales.getString("msg.command.factions.usage"));
            return true;
        }

        if(args.length == 1){
            switch(args[0]){
                case "list":
                    if(FactionsManager.getFactions().isEmpty()){
                        player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.list.no_factions"));
                        return true;
                    }
                    player.sendMessage(ChatColor.GRAY + LocalesManager.Locales.getString("msg.command.factions.list.header"));
                    for(Faction faction : FactionsManager.getFactions()){
                        player.sendMessage(ChatColor.GRAY + " - " + ChatColor.GREEN + faction.getName());
                    }
                    return true;
                case "listall":
                    if(FactionsManager.getFactions().isEmpty()){
                        player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.list.no_factions"));
                        return true;
                    }
                    player.sendMessage(ChatColor.GRAY + LocalesManager.Locales.getString("msg.command.factions.list.header"));
                    for(Faction faction : FactionsManager.getFactions()){
                        player.sendMessage(ChatColor.GRAY + " - " + ChatColor.GREEN + faction.getName());
                    }
                    return true;
                case "info":
                    if(!FactionsManager.findPlayerInFaction(player)){
                        player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.info.not_in_faction"));
                        return true;
                    }
                    Faction faction = FactionsManager.getPlayerInFaction(player).getFaction();
                    //TODO: add a list of information regarding this faction. Including: name, leader, members, etc.
                    return true;
                case "leave":
                    if(!FactionsManager.findPlayerInFaction(player)){
                        player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.leave.not_in_faction"));
                        return true;
                    }
                    if(!FactionsManager.isPlayerLeader(player)) FactionsManager.leaveFaction(player);
                    player.sendMessage(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.factions.leave.success"));
                    return true;
                case "disband":
                    if(!FactionsManager.findPlayerInFaction(player)){
                        player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.disband.not_in_faction"));
                        return true;
                    }
                    if(!FactionsManager.isPlayerLeader(player)){
                        player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.disband.not_leader"));
                        return true;
                    }
                    if(FactionsManager.getPlayerFaction(player).getMembers() == null){
                        player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.disband.no_members"));
                        return true;
                    }
                    for(FactionPlayerInfo p : FactionsManager.getPlayerFaction(player).getMembers()){
                        if(Bukkit.getPlayer(p.getUUID()) == null) continue;
                        FactionsManager.getPlayer(p).sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.disband.disbanded"));
                    }
                    FactionsManager.disbandFaction(player);
                    player.sendMessage(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.factions.disband.success"));

                    return true;
            }
        }

        if(args.length == 2){
            if(args[0].equalsIgnoreCase("create")){
                if(FactionsManager.findPlayerInFaction(player)){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.create.already_in_faction"));
                    return true;
                }
                if(args[1].length() > 16){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.create.name_too_long"));
                    return true;
                }
                if(FactionsManager.factionExists(args[1])){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.create.name_already_exists"));
                    return true;
                }
                FactionsManager.createFaction(player, args[1]);
                player.sendMessage(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.factions.create.success"));
                return true;
            }

            if(args[0].equalsIgnoreCase("join")){
                if(FactionsManager.findPlayerInFaction(player)){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.join.already_in_faction"));
                    return true;
                }
                if(!FactionsManager.factionExists(args[1])){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.join.faction_not_exists"));
                    return true;
                }
                if(!FactionsManager.getFaction(args[1]).factionSettings.factionPublic){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.join.faction_not_public"));
                    return true;
                }
                try{
                    FactionsManager.joinFaction(player, args[1]);
                } catch (Exception e){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.join.error"));
                    return true;
                }
                player.sendMessage(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.factions.join.success"));
                return true;
            }

            if(args[0].equalsIgnoreCase("invite")){
                Player target = Bukkit.getPlayer(args[1]);
                if(target == null){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.invite.nonexisting_player"));
                    return true;
                }
                if((!FactionsManager.isPlayerLeader(player) && !FactionsManager.isPlayerModerator(player)) && !FactionsManager.getPlayerFaction(player).factionSettings.allowFreeJoin){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.invite.not_leader_or_moderator"));
                    return true;
                }
                if(FactionsManager.findPlayerInFaction(target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.invite.player_already_in_faction"));
                    return true;
                }
                if(FactionsManager.factionInvites.get(target.getUniqueId()) == null || FactionsManager.factionInvites.get(target.getUniqueId()).isEmpty()){
                    FactionsManager.factionInvites.put(target.getUniqueId(), new ArrayList<>());
                }
                FactionsManager.factionInvites.get(target.getUniqueId()).add(FactionsManager.getPlayerFaction(player).getLeader().getUUID());
            }

            if(args[0].equalsIgnoreCase("kick")){
                Player target = Bukkit.getPlayer(args[1]);
                if(target == null){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.kick.nonexisting_player"));
                    return true;
                }
                if(target.getUniqueId() == player.getUniqueId()){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.kick.cannot_kick_self"));
                    return true;
                }
                if(!FactionsManager.findPlayerInFaction(target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.kick.player_not_in_faction"));
                    return true;
                }
                if(FactionsManager.isPlayerLeader(target) && FactionsManager.getPlayerFaction(target).equals(FactionsManager.getPlayerFaction(player))){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.kick.cannot_kick_leader"));
                    return true;
                }
                FactionsManager.kickPlayerFromFaction(target);
                player.sendMessage(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.factions.kick.success"));
                if(target.isOnline()) target.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.kick.kicked"));
                return true;
            }

            if(args[0].equalsIgnoreCase("promote")){
                Player target = Bukkit.getPlayer(args[1]);
                if(target == null){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.promote.nonexisting_player"));
                    return true;
                }
                if(target.getUniqueId() == player.getUniqueId()){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.promote.cannot_promote_self"));
                    return true;
                }
                if(!FactionsManager.findPlayerInFaction(target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.promote.player_not_in_faction"));
                    return true;
                }
                if(FactionsManager.isPlayerLeader(target) && FactionsManager.getPlayerFaction(target).equals(FactionsManager.getPlayerFaction(player))){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.promote.cannot_promote_leader"));
                    return true;
                }
                if(FactionsManager.isPlayerModerator(target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.promote.already_moderator"));
                    return true;
                }
                FactionsManager.promotePlayer(target);
                player.sendMessage(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.factions.promote.success"));
                if(target.isOnline()) target.sendMessage(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.factions.promote.promoted"));
                return true;
            }

            if(args[0].equalsIgnoreCase("demote")){
                Player target = Bukkit.getPlayer(args[1]);
                if(target == null){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.demote.nonexisting_player"));
                    return true;
                }
                if(target.getUniqueId() == player.getUniqueId()){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.demote.cannot_demote_self"));
                    return true;
                }
                if(!FactionsManager.findPlayerInFaction(target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.demote.player_not_in_faction"));
                    return true;
                }
                if(FactionsManager.isPlayerLeader(target) && FactionsManager.isSameFaction(player, target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.demote.cannot_demote_leader"));
                    return true;
                }
                if(!FactionsManager.isSameFaction(player, target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.demote.not_same_faction"));
                    return true;
                }
                if(!FactionsManager.isPlayerModerator(target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.demote.not_moderator"));
                    return true;
                }
                FactionsManager.demotePlayer(target);
                player.sendMessage(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.factions.demote.success"));
                if(target.isOnline()) target.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.demote.demoted"));
                return true;
            }

            if (args[0].equalsIgnoreCase("prefix")){
                if(args.length == 1 || args[1].isEmpty() || args[1].equalsIgnoreCase("")){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.prefix.no_prefix"));
                    return true;
                }
                String prefix = "";
                for(int i = 1; i < args.length; i++){
                    prefix += args[i] + " ";
                }
                prefix = prefix.substring(0, prefix.length() - 1);
                if(prefix.length() > 16){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.prefix.prefix_too_long"));
                    return true;
                }
                FactionsManager.setFactionPrefix(player, prefix);
                player.sendMessage(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.factions.prefix.success"));
                return true;
            }

            if (args[0].equalsIgnoreCase("suffix")){
                if(args.length == 1 || args[1].isEmpty() || args[1].equalsIgnoreCase("")){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.suffix.no_suffix"));
                    return true;
                }
                String suffix = "";
                for(int i = 1; i < args.length; i++){
                    suffix += args[i] + " ";
                }
                suffix = suffix.substring(0, suffix.length() - 1);
                if(suffix.length() > 16){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.suffix.suffix_too_long"));
                    return true;
                }
                FactionsManager.setFactionSuffix(player, suffix);
                player.sendMessage(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.factions.suffix.success"));
                return true;
            }

            if(args[0].equalsIgnoreCase("color")){
                if(args.length == 1 || args[1].isEmpty() || args[1].equalsIgnoreCase("")){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.color.no_color"));
                    return true;
                }
                String color = args[1];
                ChatColor chatColor;
                try{
                    chatColor = ChatColor.valueOf(color);
                }catch (Exception e){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.color.invalid_color"));
                    return true;
                }
                if(chatColor == null){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.color.invalid_color"));
                    return true;
                }
                FactionsManager.setFactionColor(player, color);
                player.sendMessage(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.factions.color.success"));
                return true;
            }
        }
        if(args.length == 3){
            if(args[0].equalsIgnoreCase("rules")){
                if(args.length == 1 || args[1].isEmpty() || args[1].equalsIgnoreCase("")){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.rules.no_rules"));
                    return true;
                }
                String rules = "";
                for(int i = 1; i < args.length; i++){
                    rules += args[i] + " ";
                }
                rules = rules.substring(0, rules.length() - 1);
                if(rules.length() > 100){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.rules.rules_too_long"));
                    return true;
                }
                try {
                    FactionsManager.setFactionRules(player, rules, args[2]);
                } catch (Exception e){
                    player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.rules.invalid_key_or_value"));
                    return true;
                }
                player.sendMessage(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.factions.rules.success"));
                return true;
            }

            if(args[0].equalsIgnoreCase("invites") && (args[1].equalsIgnoreCase("accept") || args[1].equalsIgnoreCase("deny"))){
                List<UUID> inviters = FactionsManager.factionInvites.get(player);
                inviters.removeIf(uuid -> Bukkit.getPlayer(uuid) == null);

                //TODO: Finish this; The factionsInvites is currently a HashMap<UUID, List<UUID>> which is not okay; change it to sth like HashMap<UUID, HashMap<UUID, Faction>> where the invite includes the Faction, making it easier to write code here.
                if(args[1].equalsIgnoreCase("accept")){
                    //TODO: finish /factions invites accept <Faction Name>
                    /*
                    try{
                        FactionsManager.joinFaction(player, args[2]);
                        player.sendMessage(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.factions.invites.accepted"));
                        return true;
                    } catch (Exception e){
                        player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.invites.inviter_invalid_faction"));
                        return true;
                    }*/
                }else if (args[1].equalsIgnoreCase("deny")){
                    //TODO: finish /factions invites deny <Faction Name>
                    /*
                    player.sendMessage(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.factions.invites.denied"));
                    for(UUID i : FactionsManager.factionInvites.get(player.getUniqueId())){
                        if(Bukkit.getPlayer(i) != null){
                            if(FactionsManager.isSameFaction(FactionsManager.getFaction(args[2]), FactionsManager.getPlayerFaction(Bukkit.getPlayer(i)))){
                                Bukkit.getPlayer(i).sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.invites.denied_by_player").replace("%player%", player.getName()));
                            }
                        }
                    }

                    return true;
                    */
                }
            }
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
            suggestions.add("invites");
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
            }else if (args[0].equalsIgnoreCase("invites")){
                if(FactionsManager.findPlayerInFaction(player)){
                    suggestions.add("accept");
                    suggestions.add("deny");
                    suggestions.add("list");
                }
            }
        } else if (args.length == 3){
            if(args[0].equalsIgnoreCase("invites") && (args[1].equalsIgnoreCase("accept") || args[1].equalsIgnoreCase("deny"))){
                try{
                    FactionsManager.factionInvites.get(player.getUniqueId());
                }catch (Exception e){
                    return null;
                }
                if(FactionsManager.factionInvites.get(player.getUniqueId()).isEmpty()) return null;

                for(UUID u : FactionsManager.factionInvites.get(player.getUniqueId())){
                    suggestions.add(FactionsManager.getPlayerFaction(u).getName());
                }
            }
        } else {
            // No suggestions for other arguments
            return null;
        }
        return suggestions;
    }
}
