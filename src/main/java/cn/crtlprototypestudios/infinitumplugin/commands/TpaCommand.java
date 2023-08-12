package cn.crtlprototypestudios.infinitumplugin.commands;

import cn.crtlprototypestudios.infinitumplugin.managers.LocalesManager;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TpaCommand implements CommandExecutor, TabCompleter {
    private HashMap<UUID, ArrayList<UUID>> tpaRequests = new HashMap<>();

    public TextComponent createRequestMessage(String senderName, String receiverName) {
        TextComponent message = new TextComponent(ChatColor.AQUA + String.format(LocalesManager.Locales.getString("msg.command.tpa.request_message"),ChatColor.GOLD + senderName,ChatColor.GOLD + senderName,ChatColor.GOLD + receiverName));

        TextComponent acceptButton = new TextComponent(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.tpa.accept_request_button"));
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"));
        acceptButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(LocalesManager.Locales.getString("msg.command.tpa.accept_request_button_tip"))));

        TextComponent denyButton = new TextComponent(ChatColor.RED + LocalesManager.Locales.getString("msg.command.tpa.deny_request_button"));
        denyButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny"));
        denyButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(LocalesManager.Locales.getString("msg.command.tpa.deny_request_button_tip"))));

        message.addExtra(acceptButton);
        message.addExtra(" ");
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

        if(command.getName().equalsIgnoreCase("tpa")) {
            if(args.length != 1 || args[0].equals("help")) {
                player.sendMessage(ChatColor.GRAY + LocalesManager.Locales.getString("msg.command.tpa.usage"));
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if(target == null) {
                player.sendMessage(ChatColor.RED + String.format(LocalesManager.Locales.getString("msg.command.tpa.null_player"),(ChatColor.GOLD + args[0])));
                return true;
            }

            if(target.equals(player)) {
                player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.tpa.tp_to_self"));
                return true;
            }

            if(tpaRequests.get(target.getUniqueId()) == null) tpaRequests.put(target.getUniqueId(), new ArrayList<>(1));

            tpaRequests.get(target.getUniqueId()).add(player.getUniqueId());
            target.sendMessage(createRequestMessage(player.getName(),target.getName()));
            player.sendMessage(ChatColor.AQUA + String.format(LocalesManager.Locales.getString("msg.command.tpa.sent_request"),(ChatColor.GOLD + target.getName())));
            return true;

        } else if(command.getName().equalsIgnoreCase("tpaccept")) {
            if (!tpaRequests.containsKey(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.tpa.no_pending"));
                return true;
            }

            if(args.length < 1 || args[0].equals("help")){
                player.sendMessage(ChatColor.GRAY + LocalesManager.Locales.getString("msg.command.tpaccept.usage"));
                return true;
            }

            ArrayList<UUID> requesters = tpaRequests.get(player.getUniqueId());

            if (requesters.isEmpty()) {
                player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.tpa.not_online"));
                tpaRequests.remove(player.getUniqueId());
                return true;
            }

            Player requester = null;

            try{
                requester = Bukkit.getPlayer(args[0]);
            }catch (Exception e){
                player.sendMessage(ChatColor.RED + String.format(LocalesManager.Locales.getString("msg.command.tpaccept.unavailable_sender"),(ChatColor.GOLD + args[0])));
                //tpaRequests.remove(player.getUniqueId());
                return true;
            }

            if(requesters.contains(requester.getUniqueId())){
                requester.teleport(player);
                tpaRequests.get(player.getUniqueId()).remove(requester.getUniqueId());

                player.sendMessage(ChatColor.AQUA + String.format(LocalesManager.Locales.getString("msg.command.tpaccept.success"),(ChatColor.GOLD + requester.getName())));
                requester.sendMessage(ChatColor.GREEN + String.format(LocalesManager.Locales.getString("msg.command.tpa.request_accepted"),(ChatColor.GOLD + player.getName())));
            }else{
                player.sendMessage(ChatColor.RED + String.format(LocalesManager.Locales.getString("msg.command.tpaccept.unavailable_sender"),(ChatColor.GOLD + args[0])));
            }

            return true;

        } else if(command.getName().equalsIgnoreCase("tpdeny")) {
            if (!tpaRequests.containsKey(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.tpa.no_pending"));
                return true;
            }

            if(args.length < 1 || args[0].equals("help")){
                player.sendMessage(ChatColor.GRAY + LocalesManager.Locales.getString("msg.command.tpdeny.usage"));
                return true;
            }

            ArrayList<UUID> requesters = tpaRequests.get(player.getUniqueId());

            if (requesters.isEmpty()) {
                player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.tpa.not_online"));
                tpaRequests.remove(player.getUniqueId());
                return true;
            }

            Player requester = null;

            try{
                requester = Bukkit.getPlayer(args[0]);
            }catch (Exception e){
                player.sendMessage(ChatColor.RED + String.format(LocalesManager.Locales.getString("msg.command.tpdeny.unavailable_sender"),(ChatColor.GOLD + args[0])));
                //tpaRequests.remove(player.getUniqueId());
                return true;
            }

            if(requesters.contains(requester.getUniqueId())){
                tpaRequests.get(player.getUniqueId()).remove(requester.getUniqueId());

                player.sendMessage(ChatColor.AQUA + String.format(LocalesManager.Locales.getString("msg.command.tpdeny.success"),(ChatColor.GOLD + requester.getName())));
                requester.sendMessage(ChatColor.GREEN + String.format(LocalesManager.Locales.getString("msg.command.tpa.request_denied"),(ChatColor.GOLD + player.getName())));
            }else{
                player.sendMessage(ChatColor.RED + String.format(LocalesManager.Locales.getString("msg.command.tpdeny.unavailable_sender"),(ChatColor.GOLD + args[0])));
            }

            return true;

        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;
        if(command.getName().equalsIgnoreCase("tpa")) {
            if (args.length <= 1) {
                // Provide suggestions for the first argument ("/tpa <arg>")
                List<String> suggestions = new ArrayList<>();
                Bukkit.getOnlinePlayers().forEach((p) -> {
                    if (!p.equals(player)) {
                        suggestions.add(p.getName());
                    }
                });
                return suggestions;
            }
        }else if(command.getName().equalsIgnoreCase("tpaccept") || command.getName().equalsIgnoreCase("tpdeny")) {
            if (args.length <= 1) {
                // Provide suggestions for the first argument ("/tpa <arg>")
                List<String> suggestions = new ArrayList<>();
                tpaRequests.get(player.getUniqueId()).forEach((p) -> {
                    if (!p.equals(player)) {
                        try {
                            suggestions.add(Bukkit.getPlayer(p).getName());
                        }catch (Exception e){
                            tpaRequests.get(player.getUniqueId()).remove(p);
                        }
                    }
                });
                return suggestions;
            }
        }
        return null;
    }
}
