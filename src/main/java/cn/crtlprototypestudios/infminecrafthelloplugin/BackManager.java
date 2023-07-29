package cn.crtlprototypestudios.infminecrafthelloplugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class BackManager {

    private static HashMap<UUID, Location> backs = new HashMap<>();

    public static void setBackLocation(Player player, Location location) {
        backs.put(player.getUniqueId(), location);
    }

    public static Location getBackLocation(Player player) {
        return backs.get(player.getUniqueId());
    }

}