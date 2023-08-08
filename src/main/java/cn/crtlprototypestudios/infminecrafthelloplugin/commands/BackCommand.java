package cn.crtlprototypestudios.infminecrafthelloplugin.commands;

import cn.crtlprototypestudios.infminecrafthelloplugin.managers.DeathManager;
import cn.crtlprototypestudios.infminecrafthelloplugin.managers.LocalesManager;
import jdk.vm.ci.meta.Local;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.Locale;
import java.util.ResourceBundle;

public class BackCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) commandSender;

        LocalesManager.fetchPlayerLocale(player);

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.player_exclusive"));
            return true;
        }

        if(DeathManager.getDeathLocation(player) != null){
            player.teleport(DeathManager.getDeathLocation(player));
            player.sendMessage(ChatColor.AQUA + LocalesManager.Locales.getString("msg.command.back.run_success"));
        }else{
            player.sendMessage(ChatColor.RED + LocalesManager.Locales.getString("msg.command.back.run_failure"));
        }
        DeathManager.clearDeathLocation(player);
        return true;
    }
}
