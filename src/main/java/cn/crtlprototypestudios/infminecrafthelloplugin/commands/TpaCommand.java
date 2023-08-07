package cn.crtlprototypestudios.infminecrafthelloplugin.commands;

import cn.crtlprototypestudios.infminecrafthelloplugin.managers.LocalesManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class TpaCommand implements CommandExecutor {
    private HashMap<UUID, UUID> tpaRequests = new HashMap<>();

    public TextComponent createRequestMessage(String senderName, String receiverName) {
        TextComponent message = new TextComponent(ChatColor.AQUA + LocalesManager.Locales.getString("msg.command.tpa.request_message").format(ChatColor.GOLD + senderName,ChatColor.GOLD + senderName,ChatColor.GOLD + receiverName));

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
                player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.tpa.null_player").format(args[0]));
                return true;
            }

            if(target.equals(player)) {
                player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.tpa.tp_to_self"));
                return true;
            }

            tpaRequests.put(target.getUniqueId(), player.getUniqueId());
            target.sendMessage(createRequestMessage(player.getName(),target.getName()));
            player.sendMessage(ChatColor.AQUA + LocalesManager.Locales.getString("msg.command.tpa.sent_request").format(ChatColor.GOLD + target.getName()));
            return true;

        } else if(command.getName().equalsIgnoreCase("tpaccept")) {
            if (!tpaRequests.containsKey(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.tpa.no_pending"));
                return true;
            }

            Player requester = Bukkit.getPlayer(tpaRequests.get(player.getUniqueId()));

            if (requester == null) {
                player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.tpa.not_online"));
                tpaRequests.remove(player.getUniqueId());
                return true;
            }

            requester.teleport(player);
            tpaRequests.remove(player.getUniqueId());

            player.sendMessage(ChatColor.AQUA + LocalesManager.Locales.getString("msg.command.tpa.accepted_request").format(ChatColor.GOLD + requester.getName()));
            requester.sendMessage(ChatColor.GREEN + LocalesManager.Locales.getString("msg.command.tpa.request_accepted").format(ChatColor.GOLD + player.getName()));

            return true;

        } else if(command.getName().equalsIgnoreCase("tpdeny")) {
            if (!tpaRequests.containsKey(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.tpa.no_pending"));
                return true;
            }

            Player requester = Bukkit.getPlayer(tpaRequests.get(player.getUniqueId()));

            if (requester == null) {
                player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.tpa.not_online"));
                tpaRequests.remove(player.getUniqueId());
                return true;
            }

            tpaRequests.remove(player.getUniqueId());

            player.sendMessage(ChatColor.AQUA + LocalesManager.Locales.getString("msg.command.tpa.denied_request").format(ChatColor.GOLD + requester.getName()));
            requester.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.tpa.request_denied").format(ChatColor.GOLD + player.getName()));

            return true;

        }
        return false;
    }
}
