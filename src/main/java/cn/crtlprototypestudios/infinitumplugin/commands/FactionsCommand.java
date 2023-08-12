package cn.crtlprototypestudios.infinitumplugin.commands;

import cn.crtlprototypestudios.infinitumplugin.classes.factions.Faction;
import cn.crtlprototypestudios.infinitumplugin.classes.factions.FactionInvite;
import cn.crtlprototypestudios.infinitumplugin.classes.factions.FactionPlayerInfo;
import cn.crtlprototypestudios.infinitumplugin.classes.factions.FactionSettings;
import cn.crtlprototypestudios.infinitumplugin.managers.FactionsManager;
import cn.crtlprototypestudios.infinitumplugin.managers.LocalesManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
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

public class FactionsCommand implements CommandExecutor, TabCompleter {
    public void composeListMessage(Faction faction, Player player){
        /*
        Faction Information:
            - Name: <faction name>
            - Owner: <faction owner>
            - Money: <faction money>
            - Moderators:
                -> <faction moderator 1>
                -> <faction moderator 2>
                ...
                -> <faction moderator n>
            - Members:
                -> <faction member 1>
                -> <faction member 2>
                ...
                -> <faction member n>
            - Allied Factions:
                -> <faction allied 1>
                -> <faction allied 2>
                ...
                -> <faction allied n>
            - Enemy Factions:
                -> <faction enemy 1>
                -> <faction enemy 2>
                ...
                -> <faction enemy n>
         */
        player.sendMessage(ChatColor.GOLD + LocalesManager.getProp("msg.command.factions.info.header"));
        player.sendMessage(ChatColor.DARK_GRAY + "    - " + ChatColor.GRAY + LocalesManager.getPropFormatted("msg.command.factions.info.name",faction.getColor() + faction.getName()));
        player.sendMessage(ChatColor.DARK_GRAY + "    - " + ChatColor.GRAY + LocalesManager.getPropFormatted("msg.command.factions.info.owner", ChatColor.BLUE + faction.getLeader().getPlayer().getName()));
        player.sendMessage(ChatColor.DARK_GRAY + "    - " + ChatColor.GRAY + LocalesManager.getPropFormatted("msg.command.factions.info.money", ChatColor.BLUE + String.valueOf(faction.getVault().getMoney())));
        player.sendMessage(ChatColor.DARK_GRAY + "    - " + ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.info.moderators"));
        for(FactionPlayerInfo moderator : faction.getModerators()){
            player.sendMessage(ChatColor.DARK_GRAY + "        -> " + ChatColor.BLUE + moderator.getPlayer().getName());
        }
        player.sendMessage(ChatColor.DARK_GRAY + "    - " + ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.info.members"));
        for(FactionPlayerInfo member : faction.getMembers()){
            player.sendMessage(ChatColor.DARK_GRAY + "        -> " + ChatColor.BLUE + member.getPlayer().getName());
        }
        player.sendMessage(ChatColor.DARK_GRAY + "    - " + ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.info.allied"));
        for(Faction allied : faction.getAlliedFactions()){
            player.sendMessage(ChatColor.DARK_GRAY + "        -> " + ChatColor.BLUE + allied.getName());
        }
        player.sendMessage(ChatColor.DARK_GRAY + "    - " + ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.info.enemies"));
        for(Faction enemy : faction.getEnemyFactions()){
            player.sendMessage(ChatColor.DARK_GRAY + "        -> " + ChatColor.BLUE + enemy.getName());
        }
    }
    public void composeFactionsList(List<Faction> factions, Player player){
        /*
        Factions List:
            - <faction 1>
            - <faction 2>
            ...
            - <faction n>
         */
        boolean isPlayerOp = player.isOp();
        TextComponent message = new TextComponent();
        player.sendMessage(ChatColor.GOLD + LocalesManager.Locales.getString("msg.command.factions.list.header"));
        for(Faction faction : factions){
            player.sendMessage(ChatColor.DARK_GRAY + "    - " + ChatColor.YELLOW + LocalesManager.getPropFormatted("msg.command.factions.list.name",faction.getColor() + faction.getName()));
            player.sendMessage(ChatColor.DARK_GRAY + "        -> " + ChatColor.GRAY + LocalesManager.getPropFormatted("msg.command.factions.list.owner",((faction.factionSettings.leadersPublic ? "" : ChatColor.MAGIC) + (ChatColor.BLUE + faction.getLeader().getPlayer().getName()))));
            player.sendMessage(ChatColor.DARK_GRAY + "        -> " + ChatColor.GRAY + LocalesManager.getPropFormatted("msg.command.factions.list.moderators",((faction.factionSettings.moderatorsPublic ? "" : ChatColor.MAGIC) + (ChatColor.BLUE + String.valueOf(faction.getModerators().size())))));
            player.sendMessage(ChatColor.DARK_GRAY + "        -> " + ChatColor.GRAY + LocalesManager.getPropFormatted("msg.command.factions.list.members",((faction.factionSettings.moderatorsPublic ? "" : ChatColor.MAGIC) + (ChatColor.BLUE + String.valueOf(faction.getMembers().size())))));
            player.sendMessage(ChatColor.DARK_GRAY + "        -> " + ChatColor.GRAY + LocalesManager.getPropFormatted("msg.command.factions.list.allied",((faction.factionSettings.alliedFactionsPublic ? "" : ChatColor.MAGIC) + (ChatColor.BLUE + String.valueOf(faction.getAlliedFactions().size())))));
            player.sendMessage(ChatColor.DARK_GRAY + "        -> " + ChatColor.GRAY + LocalesManager.getPropFormatted("msg.command.factions.list.enemies",((faction.factionSettings.enemyFactionsPublic ? "" : ChatColor.MAGIC) + (ChatColor.BLUE + String.valueOf(faction.getEnemyFactions().size())))));
        }
    }
    public TextComponent createInviteMessage(List<FactionInvite> factionInvite){
        /*
        Faction Invites:
            - Invite from <Faction Name> sent by <Sender Name>. [Join] [Deny]
         */
        TextComponent message = new TextComponent();
        message.addExtra(ChatColor.GOLD + LocalesManager.Locales.getString("msg.command.factions.invite.header"));

        for(FactionInvite invite : factionInvite){
            TextComponent line = new TextComponent();
            line.setText(ChatColor.GRAY + "    - " + String.format(LocalesManager.Locales.getString("msg.command.factions.invite.body_message"),invite.faction.getName(), invite.sender.getUsername()) + " ");

            TextComponent joinButton = new TextComponent();
            joinButton.setText(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.factions.invite.join"));
            joinButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/factions invites accept " + invite.faction.getName()));
            joinButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.factions.invite.join_hover"))));

            TextComponent denyButton = new TextComponent();
            denyButton.setText(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.invite.deny"));
            denyButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/factions invites deny " + invite.faction.getName()));
            denyButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.invite.deny_hover"))));

            line.addExtra(joinButton);
            line.addExtra(ChatColor.GRAY + " ");
            line.addExtra(denyButton);

            message.addExtra(line);
        }
        return message;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) commandSender;
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.player_exclusive"));
            return true;
        }

        if((args.length <= 1 || args.length == 0) && !(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("listall") || args[0].equalsIgnoreCase("info"))){
            player.sendMessage(ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.usage"));
            return true;
        }

        if(args.length == 1){
            switch(args[0]){
                case "create":
                    player.sendMessage(ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.create.usage"));
                    return true;
                case "list":
                    if(FactionsManager.getFactions().isEmpty()){
                        player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.list.no_factions"));
                        return true;
                    }
                    composeFactionsList(FactionsManager.getFactions(), player);
                    return true;
                case "listall":
                    if(!player.isOp()){
                        player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.listall.no_permission"));
                        return true;
                    }
                    if(FactionsManager.getFactions().isEmpty()){
                        player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.listall.no_factions"));
                        return true;
                    }
                    composeFactionsList(FactionsManager.getFactions(), player);
                    return true;
                case "info":
                    if(!FactionsManager.findPlayerInFaction(player)){
                        player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.factions.info.not_in_faction"));
                        return true;
                    }
                    Faction faction = FactionsManager.getPlayerInFaction(player).getFaction();

                    composeListMessage(faction,player);
                    return true;
                case "leave":
                    if(!FactionsManager.findPlayerInFaction(player)){
                        player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.leave.not_in_faction"));
                        return true;
                    }
                    if(!FactionsManager.isPlayerLeader(player)) FactionsManager.leaveFaction(player);
                    player.sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.leave.success",FactionsManager.getPlayerFaction(player).getName()));
                    return true;
                case "disband":
                    if(!FactionsManager.findPlayerInFaction(player)){
                        player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.disband.not_in_faction"));
                        return true;
                    }
                    if(!FactionsManager.isPlayerLeader(player)){
                        player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.disband.not_leader"));
                        return true;
                    }
                    if(FactionsManager.getPlayerFaction(player).getMembers() == null){
                        player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.disband.no_members"));
                        return true;
                    }
                    for(FactionPlayerInfo p : FactionsManager.getPlayerFaction(player).getMembers()){
                        if(Bukkit.getPlayer(p.getUUID()) == null) continue;
                        FactionsManager.getPlayer(p).sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.disband.disbanded",FactionsManager.getPlayerFaction(player).getName()));
                    }
                    FactionsManager.disbandFaction(player);
                    player.sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.disband.success",FactionsManager.getPlayerFaction(player).getName()));

                    return true;
                case "rules":
                    player.sendMessage(ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.rules.usage"));
                    return true;
                case "invites":
                    player.sendMessage(ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.invites.usage"));
                    return true;
                case "join":
                    player.sendMessage(ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.join.usage"));
                    return true;
                case "kick":
                    player.sendMessage(ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.kick.usage"));
                    return true;
                case "promote":
                    player.sendMessage(ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.promote.usage"));
                    return true;
                case "demote":
                    player.sendMessage(ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.demote.usage"));
                    return true;
                case "invite":
                    player.sendMessage(ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.invite.usage"));
                    return true;
                case "prefix":
                    player.sendMessage(ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.prefix.usage"));
                    return true;
                case "suffix":
                    player.sendMessage(ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.suffix.usage"));
                    return true;
                case "color":
                    player.sendMessage(ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.color.usage"));
                    return true;
                case "ally":
                    player.sendMessage(ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.ally.usage"));
                    return true;
                case "enemy":
                    player.sendMessage(ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.enemy.usage"));
                    return true;
                case "neutral":
                    player.sendMessage(ChatColor.GRAY + LocalesManager.getProp("msg.command.factions.neutral.usage"));
                    return true;
            }
        }

        if(args.length == 2){
            if(args[0].equalsIgnoreCase("prefix") || args[0].equalsIgnoreCase("suffix")){
                player.sendMessage(ChatColor.GRAY + LocalesManager.getProp(args[0].equalsIgnoreCase("prefix") ? "msg.command.factions.prefix.usage" : "msg.command.factions.suffix.usage"));
                return true;
            }

            if(args[0].equalsIgnoreCase("create")){
                if(FactionsManager.findPlayerInFaction(player)){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.create.already_in_faction"));
                    return true;
                }
                if(args[1].length() > 16){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.create.name_too_long"));
                    return true;
                }
                if(FactionsManager.factionExists(args[1])){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.create.name_already_exists"));
                    return true;
                }
                FactionsManager.createFaction(player, args[1]);
                player.sendMessage(ChatColor.GREEN + LocalesManager.getProp("msg.command.factions.create.success"));
                return true;
            }

            if(args[0].equalsIgnoreCase("join")){
                if(FactionsManager.findPlayerInFaction(player)){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.join.already_in_faction"));
                    return true;
                }
                if(!FactionsManager.factionExists(args[1])){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.join.faction_not_exists"));
                    return true;
                }
                if(!FactionsManager.getFaction(args[1]).factionSettings.factionPublic){
                    player.sendMessage(ChatColor.RED + (FactionsManager.getFaction(args[1]).factionSettings.factionPublic ? LocalesManager.Locales.getString("msg.command.factions.join.faction_not_public") : LocalesManager.Locales.getString("msg.command.factions.join.faction_not_public_hidden")));
                    return true;
                }
                try{
                    FactionsManager.joinFaction(player, args[1]);
                } catch (Exception e){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.join.error"));
                    return true;
                }
                player.sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.join.success", ChatColor.GOLD + args[1]));
                return true;
            }

            if(args[0].equalsIgnoreCase("invite")){
                Player target = Bukkit.getPlayer(args[1]);
                if(target == null){
                    player.sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.invite.nonexisting_player",args[1]));
                    return true;
                }
                if((!FactionsManager.isPlayerLeader(player) && !FactionsManager.isPlayerModerator(player)) && !FactionsManager.getPlayerFaction(player).factionSettings.allowFreeJoin){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.invite.not_leader_or_moderator"));
                    return true;
                }
                if(FactionsManager.findPlayerInFaction(target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.invite.player_already_in_faction", target.getName()));
                    return true;
                }
                if(FactionsManager.factionInvites.get(target.getUniqueId()) == null || FactionsManager.factionInvites.get(target.getUniqueId()).isEmpty()){
                    FactionsManager.factionInvites.put(target.getUniqueId(), new ArrayList<>());
                }
                FactionsManager.factionInvites.get(target.getUniqueId()).add(new FactionInvite(player, FactionsManager.getPlayerFaction(player)));
            }

            if(args[0].equalsIgnoreCase("invites")){
                List<FactionInvite> inviters = FactionsManager.factionInvites.get(player);
                if (args[0].equalsIgnoreCase("list")) {
                    if (inviters == null || inviters.isEmpty()) {
                        player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.invites.no_invites"));
                        return true;
                    }
                    player.sendMessage(createInviteMessage(inviters));
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.invites.usage"));
                    return true;
                }
            }

            if(args[0].equalsIgnoreCase("kick")){
                Player target = Bukkit.getPlayer(args[1]);
                if(target == null){
                    player.sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.kick.nonexisting_player",args[1]));
                    return true;
                }
                if(target.getUniqueId() == player.getUniqueId()){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.kick.cannot_kick_self"));
                    return true;
                }
                if(!FactionsManager.findPlayerInFaction(target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.kick.player_not_in_faction",target.getName()));
                    return true;
                }
                if(FactionsManager.isPlayerLeader(target) && FactionsManager.getPlayerFaction(target).equals(FactionsManager.getPlayerFaction(player))){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.kick.cannot_kick_leader"));
                    return true;
                }
                FactionsManager.kickPlayerFromFaction(target);
                player.sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.kick.success",target.getName()));
                if(target.isOnline()) target.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.kick.kicked"));
                return true;
            }

            if(args[0].equalsIgnoreCase("promote")){
                Player target = Bukkit.getPlayer(args[1]);
                if(target == null){
                    player.sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.promote.nonexisting_player",args[1]));
                    return true;
                }
                if(target.getUniqueId() == player.getUniqueId()){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.promote.cannot_promote_self"));
                    return true;
                }
                if(!FactionsManager.findPlayerInFaction(target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.promote.player_not_in_faction",target.getName()));
                    return true;
                }
                if(FactionsManager.isPlayerLeader(target) && FactionsManager.isSameFaction(player, target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.promote.cannot_promote_leader"));
                    return true;
                }
                if(!FactionsManager.isSameFaction(player, target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.promote.not_same_faction",target.getName()));
                    return true;
                }
                if(FactionsManager.isPlayerModerator(target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.promote.already_moderator",target.getName()));
                    return true;
                }
                FactionsManager.promotePlayer(target);
                player.sendMessage(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.factions.promote.success"));
                if(target.isOnline()) target.sendMessage(ChatColor.GREEN + String.format(LocalesManager.Locales.getString("msg.command.factions.promote.promoted"),FactionsManager.getPlayerFaction(target).getName()));
                return true;
            }

            if(args[0].equalsIgnoreCase("demote")){
                Player target = Bukkit.getPlayer(args[1]);
                if(target == null){
                    player.sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.demote.nonexisting_player", args[1]));
                    return true;
                }
                if(target.getUniqueId() == player.getUniqueId()){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.demote.cannot_demote_self"));
                    return true;
                }
                if(!FactionsManager.findPlayerInFaction(target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.demote.player_not_in_faction", args[1]));
                    return true;
                }
                if(FactionsManager.isPlayerLeader(target) && FactionsManager.isSameFaction(player, target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.demote.cannot_demote_leader"));
                    return true;
                }
                if(!FactionsManager.isSameFaction(player, target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.demote.not_same_faction", target.getName()));
                    return true;
                }
                if(!FactionsManager.isPlayerModerator(target)){
                    player.sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.demote.not_moderator",target.getName()));
                    return true;
                }
                FactionsManager.demotePlayer(target);
                player.sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.demote.success", target.getName()));
                if(target.isOnline()) target.sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.demote.demoted", FactionsManager.getPlayerFaction(target).getName()));
                return true;
            }

            if(args[0].equalsIgnoreCase("color")){
                if(args.length == 1 || args[1].isEmpty() || args[1].equalsIgnoreCase("")){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.color.no_color"));
                    return true;
                }
                String color = args[1];
                ChatColor chatColor;
                try{
                    chatColor = ChatColor.valueOf(color);
                }catch (Exception e){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.color.invalid_color"));
                    return true;
                }
                if(chatColor == null){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.color.invalid_color"));
                    return true;
                }
                FactionsManager.setFactionColor(player, color);
                player.sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.color.success", chatColor + color));
                return true;
            }

            if(args[0].equalsIgnoreCase("ally")){
                if (args.length == 1 || args[1].isEmpty() || args[1].equalsIgnoreCase("")) {
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.ally.no_faction"));
                    return true;
                }
                if (!FactionsManager.factionExists(args[1])) {
                    player.sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.ally.invalid_faction", args[1]));
                    return true;
                }
                if (!FactionsManager.findPlayerInFaction(player)) {
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.ally.not_in_faction"));
                    return true;
                }
                if (FactionsManager.isSameFaction(player, FactionsManager.getFaction(args[1]))) {
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.ally.cannot_ally_self"));
                    return true;
                }
                if (FactionsManager.getPlayerFaction(player).getAlliedFactions().contains(FactionsManager.getFaction(args[1]))) {
                    player.sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.ally.already_allied", FactionsManager.getFaction(args[1]).getName()));
                    return true;
                }
                if (FactionsManager.getPlayerFaction(player).getEnemyFactions().contains(FactionsManager.getFaction(args[1]))) {
                    FactionsManager.unenemyFactions(player, FactionsManager.getFaction(args[1]));
                }
                FactionsManager.allyFactions(player, FactionsManager.getFaction(args[1]));
                player.sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.ally.success", FactionsManager.getFaction(args[1]).getName()));
                if(Bukkit.getPlayer(FactionsManager.getFaction(args[1]).getLeader().getUUID()) != null){
                    Bukkit.getPlayer(FactionsManager.getFaction(args[1]).getLeader().getUUID()).sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.ally.ally", FactionsManager.getPlayerFaction(player).getName()));
                }
                if(FactionsManager.getFaction(args[1]).getModerators() != null){
                    for(FactionPlayerInfo member : FactionsManager.getFaction(args[1]).getModerators()){
                        if(Bukkit.getPlayer(member.getUUID()) != null){
                            Bukkit.getPlayer(member.getUUID()).sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.ally.ally", FactionsManager.getPlayerFaction(player).getName()));
                        }
                    }
                }
                return true;
            }

            if(args[0].equalsIgnoreCase("enemy")){
                if (args.length == 1 || args[1].isEmpty() || args[1].equalsIgnoreCase("")) {
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.enemy.no_faction"));
                    return true;
                }
                if (!FactionsManager.factionExists(args[1])) {
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.enemy.invalid_faction"));
                    return true;
                }
                if (!FactionsManager.findPlayerInFaction(player)) {
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.enemy.not_in_faction"));
                    return true;
                }
                if (FactionsManager.isSameFaction(player, FactionsManager.getFaction(args[1]))) {
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.enemy.cannot_enemy_self"));
                    return true;
                }
                if (FactionsManager.getPlayerFaction(player).getAlliedFactions().contains(FactionsManager.getFaction(args[1]))) {
                    FactionsManager.unallyFactions(player, FactionsManager.getFaction(args[1]));
                }
                if (FactionsManager.getPlayerFaction(player).getEnemyFactions().contains(FactionsManager.getFaction(args[1]))) {
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.enemy.already_enemy"));
                    return true;
                }
                FactionsManager.enemyFactions(player, FactionsManager.getFaction(args[1]));
                player.sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.enemy.success", FactionsManager.getFaction(args[1]).getName()));
                if(Bukkit.getPlayer(FactionsManager.getFaction(args[1]).getLeader().getUUID()) != null){
                    Bukkit.getPlayer(FactionsManager.getFaction(args[1]).getLeader().getUUID()).sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.enemy.enemy", FactionsManager.getPlayerFaction(player).getName()));
                }
                if(FactionsManager.getFaction(args[1]).getModerators() != null) {
                    for (FactionPlayerInfo member : FactionsManager.getFaction(args[1]).getModerators()) {
                        if (Bukkit.getPlayer(member.getUUID()) != null) {
                            Bukkit.getPlayer(member.getUUID()).sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.enemy.enemy", FactionsManager.getPlayerFaction(player).getName()));
                        }
                    }
                }
            }

            if(args[0].equalsIgnoreCase("neutral")){
                if (args.length == 1 || args[1].isEmpty() || args[1].equalsIgnoreCase("")) {
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.neutral.no_faction"));
                    return true;
                }
                if (!FactionsManager.factionExists(args[1])) {
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.neutral.invalid_faction"));
                    return true;
                }
                if (!FactionsManager.findPlayerInFaction(player)) {
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.neutral.not_in_faction"));
                    return true;
                }
                if (FactionsManager.isSameFaction(player, FactionsManager.getFaction(args[1]))) {
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.neutral.cannot_neutral_self"));
                    return true;
                }
                if (!FactionsManager.getPlayerFaction(player).getAlliedFactions().contains(FactionsManager.getFaction(args[1])) && !FactionsManager.getPlayerFaction(player).getEnemyFactions().contains(FactionsManager.getFaction(args[1]))) {
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.neutral.not_enemy_or_ally"));
                    return true;
                }
                FactionsManager.unallyFactions(player, FactionsManager.getFaction(args[1]));
                FactionsManager.unenemyFactions(player, FactionsManager.getFaction(args[1]));
                player.sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.neutral.success", FactionsManager.getFaction(args[1]).getName()));
                if(Bukkit.getPlayer(FactionsManager.getFaction(args[1]).getLeader().getUUID()) != null){
                    Bukkit.getPlayer(FactionsManager.getFaction(args[1]).getLeader().getUUID()).sendMessage(ChatColor.YELLOW + LocalesManager.getPropFormatted("msg.command.factions.neutral.neutral", FactionsManager.getPlayerFaction(player).getName()));
                }
                if(FactionsManager.getFaction(args[1]).getModerators() != null) {
                    for (FactionPlayerInfo member : FactionsManager.getFaction(args[1]).getModerators()) {
                        if (Bukkit.getPlayer(member.getUUID()) != null) {
                            Bukkit.getPlayer(member.getUUID()).sendMessage(ChatColor.YELLOW + LocalesManager.getPropFormatted("msg.command.factions.neutral.neutral", FactionsManager.getPlayerFaction(player).getName()));
                        }
                    }
                }
            }
        }
        if(args.length == 3){
            if (args[0].equalsIgnoreCase("prefix")){
                if(args[1].isEmpty() || args[1].equalsIgnoreCase("")){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.prefix.no_prefix"));
                    return true;
                }
                String prefix = args[1];
                prefix = prefix.substring(1, prefix.length() - 1);
                if(prefix.length() > 16){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.prefix.prefix_too_long"));
                    return true;
                }
                ChatColor prefixColor = ChatColor.valueOf(args[2]);
                if(ChatColor.getByChar(prefixColor.getChar()) == null){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.prefix.invalid_color"));
                    return true;
                }
                FactionsManager.setFactionPrefix(player, prefix);
                FactionsManager.setFactionPrefixColor(player, args[2]);
                player.sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.prefix.success", prefixColor + prefix));
                return true;
            }

            if (args[0].equalsIgnoreCase("suffix")){
                if(args[1].equalsIgnoreCase("")){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.suffix.no_suffix"));
                    return true;
                }
                String suffix = "";
                suffix = suffix.substring(1, suffix.length() - 1);
                if(suffix.length() > 16){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.suffix.suffix_too_long"));
                    return true;
                }
                ChatColor suffixColor = ChatColor.valueOf(args[2]);
                if(ChatColor.getByChar(suffixColor.getChar()) == null){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.suffix.invalid_color"));
                    return true;
                }
                FactionsManager.setFactionSuffix(player, suffix);
                FactionsManager.setFactionSuffix(player, args[2]);
                player.sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.suffix.success", suffixColor + suffix));
                return true;
            }

            if(args[0].equalsIgnoreCase("rules")){
                if(args.length == 1 || args[1].isEmpty() || args[1].equalsIgnoreCase("")){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.rules.no_rules"));
                    return true;
                }
                String rule = args[1];
                if(rule.length() > 100){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.rules.rules_too_long"));
                    return true;
                }
                String value = "java.lang.IllegalArgumentException";
                try {
                    value = FactionsManager.getFactionRuleValue(player, rule);
                    FactionsManager.setFactionRules(player, rule, args[2]);
                } catch (Exception e){
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.rules.invalid_key_or_value"));
                    return true;
                }
                player.sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.rules.success",rule, args[2], value));
                return true;
            }

            if(args[0].equalsIgnoreCase("invites") && (args[1].equalsIgnoreCase("accept") || args[1].equalsIgnoreCase("deny"))){
                List<FactionInvite> inviters = FactionsManager.factionInvites.get(player);

                if(args[1].equalsIgnoreCase("accept")){
                    if(inviters == null || inviters.isEmpty()){
                        player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.invites.no_invites"));
                        return true;
                    }
                    if(args.length == 2 || args[2].isEmpty() || args[2].equalsIgnoreCase("")){
                        player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.invites.no_faction"));
                        return true;
                    }
                    Faction invitingFaction = FactionsManager.getFaction(args[2]);
                    if(invitingFaction == null){
                        player.sendMessage(ChatColor.RED + LocalesManager.getPropFormatted("msg.command.factions.invites.invalid_faction", args[2]));
                        FactionsManager.factionInvites.get(player).clear();
                        return true;
                    }
                    for(FactionInvite i : inviters){
                        if(i.faction.equals(invitingFaction) || invitingFaction.getAllMembers(false).contains(i.sender)){
                            try{
                                FactionsManager.joinFaction(player, invitingFaction.getName());
                                player.sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.invites.accepted", invitingFaction.getName()));
                                if(Bukkit.getPlayer(i.sender.getUUID()) != null){
                                    Bukkit.getPlayer(i.sender.getUUID()).sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.invites.inviter_accepted", player.getName()));
                                }
                            } catch (Exception e){
                                player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.invites.inviter_invalid_faction"));
                            }
                            FactionsManager.factionInvites.get(player.getUniqueId()).clear();
                            return true;
                        }
                    }

                } else if (args[1].equalsIgnoreCase("deny")){
                    if(inviters == null || inviters.isEmpty()){
                        player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.invites.no_invites"));
                        return true;
                    }
                    if(args.length == 2 || args[2].isEmpty() || args[2].equalsIgnoreCase("")){
                        player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.invites.no_faction"));
                        return true;
                    }
                    Faction invitingFaction = FactionsManager.getFaction(args[2]);
                    if(invitingFaction == null){
                        player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.invites.invalid_faction"));
                        FactionsManager.factionInvites.get(player).clear();
                        return true;
                    }
                    for(FactionInvite i : inviters){
                        if(i.faction.equals(invitingFaction) || invitingFaction.getAllMembers(false).contains(i.sender)){
                            try{
                                player.sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.invites.denied", invitingFaction.getName()));
                                if(Bukkit.getPlayer(i.sender.getUUID()) != null){
                                    Bukkit.getPlayer(i.sender.getUUID()).sendMessage(ChatColor.GREEN + LocalesManager.getPropFormatted("msg.command.factions.invites.inviter_denied", player.getName()));
                                }
                            } catch (Exception e){
                                player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.invites.inviter_invalid_faction"));
                            }
                            FactionsManager.factionInvites.get(player.getUniqueId()).remove(i);
                            return true;
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + LocalesManager.getProp("msg.command.factions.invites.usage_list"));
                    return true;
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
            if(!FactionsManager.findPlayerInFaction(player)){
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
                    suggestions.add("ally");
                    suggestions.add("enemy");
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
                if(FactionsManager.isFactionNull(f)) return null;
                for (FactionPlayerInfo p : f.getMembers()) {
                    suggestions.add(p.getUsername());
                }
                return suggestions;
            }else if (args[0].equalsIgnoreCase("demote")){
                Faction f = FactionsManager.getPlayerFaction(player);
                if(FactionsManager.isFactionNull(f)) return null;
                for (FactionPlayerInfo p : f.getModerators()) {
                    suggestions.add(p.getUsername());
                }
                return suggestions;
            }else if (args[0].equalsIgnoreCase("kick")){
                FactionPlayerInfo f = FactionsManager.getPlayerInFaction(player);
                if(FactionsManager.isFactionNull(player)) return null;
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
                if(FactionsManager.isFactionNull(faction)) return null;
                for (Player p : player.getServer().getOnlinePlayers()) {
                    if(!FactionsManager.isPlayerLeader(p)) suggestions.add(p.getName());
                }
                return suggestions;
            }else if (args[0].equalsIgnoreCase("join")) {
                if(FactionsManager.isFactionNull(player)) return null;
                for (Faction f : FactionsManager.getFactions()) {
                    if(f.factionSettings.factionPublic) suggestions.add(f.getName());
                }
                return suggestions;
            }else if (args[0].equalsIgnoreCase("rules")){
                if(FactionsManager.isFactionNull(player)) return null;
                if(FactionsManager.isPlayerLeader(player)){
                    suggestions.addAll(FactionSettings.getRulesKey());
                }
            }else if (args[0].equalsIgnoreCase("invites")){
                Faction faction = FactionsManager.getPlayerFaction(player);
                if(FactionsManager.isFactionNull(faction)) return null;
                if(FactionsManager.findPlayerInFaction(player)){
                    suggestions.add("accept");
                    suggestions.add("deny");
                    suggestions.add("list");
                }
            }else if (args[0].equalsIgnoreCase("ally")){
                Faction faction = FactionsManager.getPlayerFaction(player);
                if(FactionsManager.isFactionNull(faction)) return null;
                if(FactionsManager.isPlayerLeader(player) && faction != null){
                    for(Faction f : FactionsManager.getFactions()){
                        if(!f.equals(faction) && !faction.getAlliedFactions().contains(f)){
                            suggestions.add(f.getName());
                        }
                    }
                }
            }else if (args[0].equalsIgnoreCase("enemy")){
                Faction faction = FactionsManager.getPlayerFaction(player);
                if(FactionsManager.isFactionNull(faction)) return null;
                if(FactionsManager.isPlayerLeader(player) && faction != null){
                    for(Faction f : FactionsManager.getFactions()){
                        if(!f.equals(faction) && !faction.getEnemyFactions().contains(f)){
                            suggestions.add(f.getName());
                        }
                    }
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

                for(FactionInvite i : FactionsManager.factionInvites.get(player.getUniqueId())){
                    suggestions.add(i.faction.getName());
                }
                return suggestions;
            }else if(args[0].equalsIgnoreCase("prefix") || args[0].equalsIgnoreCase("suffix")){
                for(ChatColor c : ChatColor.values()){
                    suggestions.add(c.name());
                }
                return suggestions;
            }
        } else {
            // No suggestions for other arguments
            return null;
        }
        return suggestions;
    }
}
