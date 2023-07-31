package cn.crtlprototypestudios.infminecrafthelloplugin.managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class DeathManager {

    private static HashMap<UUID, Location> deathLocations = new HashMap<>();

    public static void setDeathLocation(Player player, Location location) {
        deathLocations.put(player.getUniqueId(), location);
    }

    public static Location getDeathLocation(Player player) {
        return deathLocations.get(player.getUniqueId()) == null ? null : deathLocations.get(player.getUniqueId());
    }

    public static void clearDeathLocation(Player player) {
        deathLocations.remove(player.getUniqueId());
    }

}