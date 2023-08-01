package cn.crtlprototypestudios.infminecrafthelloplugin.commands;

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

    public TextComponent createRequestMessage(String senderName) {
        TextComponent message = new TextComponent((ChatColor.GOLD + senderName) + (ChatColor.AQUA + " has sent you a request. "));

        TextComponent acceptButton = new TextComponent(ChatColor.GREEN + "[Accept]");
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"));
        acceptButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Click to Accept Request")));

        TextComponent denyButton = new TextComponent(ChatColor.RED + "[Deny]");
        denyButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny"));
        denyButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Click to Deny Request")));

        message.addExtra(acceptButton);
        message.addExtra(" ");
        message.addExtra(denyButton);

        return message;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) commandSender;

        if(command.getName().equalsIgnoreCase("tpa")) {
            if(args.length != 1 || args[0].equals("help")) {
                player.sendMessage(ChatColor.GRAY + "Usage: /tpa <Player Username>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if(target == null) {
                player.sendMessage(ChatColor.RED + ("The Player " + args[0] + " does not exist."));
                return true;
            }

            if(target.equals(player)) {
                player.sendMessage(ChatColor.RED + "You cannot teleport to yourself!");
                return true;
            }

            tpaRequests.put(target.getUniqueId(), player.getUniqueId());
            target.sendMessage(createRequestMessage(player.getName()));
            player.sendMessage((ChatColor.AQUA + "You have sent a teleport request to ") + (ChatColor.GOLD + target.getName()) + (ChatColor.AQUA + "."));
            return true;

        } else if(command.getName().equalsIgnoreCase("tpaccept")) {
            if (!tpaRequests.containsKey(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You have no pending requests!");
                return true;
            }

            Player requester = Bukkit.getPlayer(tpaRequests.get(player.getUniqueId()));

            if (requester == null) {
                player.sendMessage(ChatColor.RED + "That player is no longer online!");
                tpaRequests.remove(player.getUniqueId());
                return true;
            }

            requester.teleport(player);
            tpaRequests.remove(player.getUniqueId());

            player.sendMessage((ChatColor.AQUA + "Accepted teleport request from ") + (ChatColor.GOLD + requester.getName()) + (ChatColor.AQUA + "."));
            requester.sendMessage((ChatColor.GOLD + player.getName()) + (ChatColor.AQUA + " accepted your teleport request."));

            return true;

        } else if(command.getName().equalsIgnoreCase("tpdeny")) {
            if (!tpaRequests.containsKey(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You have no pending requests!");
                return true;
            }

            Player requester = Bukkit.getPlayer(tpaRequests.get(player.getUniqueId()));

            if (requester == null) {
                player.sendMessage(ChatColor.RED + "That player is no longer online!");
                tpaRequests.remove(player.getUniqueId());
                return true;
            }

            tpaRequests.remove(player.getUniqueId());

            player.sendMessage((ChatColor.AQUA + "Rejected teleport request from ") + (ChatColor.GOLD + requester.getName()) + (ChatColor.AQUA + "."));
            requester.sendMessage((ChatColor.GOLD + player.getName()) + (ChatColor.RED + " rejected your teleport request."));

            return true;

        }
        return false;
    }
}
