package cn.crtlprototypestudios.infminecrafthelloplugin.listeners;

import cn.crtlprototypestudios.infminecrafthelloplugin.managers.DeathManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerEventListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();

        // Store death location
        Location deathLoc = player.getLocation();
        DeathManager.setDeathLocation(player, deathLoc);
    }
}
