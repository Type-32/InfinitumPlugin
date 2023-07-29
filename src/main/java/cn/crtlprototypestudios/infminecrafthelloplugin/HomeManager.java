package cn.crtlprototypestudios.infminecrafthelloplugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class HomeManager {

    private static HashMap<UUID, Location> homes = new HashMap<>();

    public static void setHome(Player player, Location location) {
        homes.put(player.getUniqueId(), location);
    }

    public static Location getHome(Player player) {
        return homes.get(player.getUniqueId());
    }

}