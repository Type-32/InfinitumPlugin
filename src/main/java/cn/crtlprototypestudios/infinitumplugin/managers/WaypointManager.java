package cn.crtlprototypestudios.infinitumplugin.managers;

import cn.crtlprototypestudios.infinitumplugin.InfinitumPlugin;
import cn.crtlprototypestudios.infinitumplugin.classes.waypoints.Waypoint;
import cn.crtlprototypestudios.infinitumplugin.classes.waypoints.WaypointList;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class WaypointManager {

    private static HashMap<UUID, WaypointList> waypoints = new HashMap<>();
    public static HashMap<UUID, HashMap<UUID, List<Waypoint>>> sharedWaypoints = new HashMap<>();

    public static List<UUID> getPlayers(){
        return new ArrayList<>(waypoints.keySet());
    }
    public static Waypoint getWaypoint(Player player, String name){
        preventNull(player);
        return waypoints.get(player.getUniqueId()).getWaypoint(name);
    }
    public static WaypointList getWaypointList(Player player){
        preventNull(player);
        return waypoints.get(player.getUniqueId());
    }
    public static void shareWaypoint(Player player, Player target, Waypoint waypoint){
        preventNull(player);
        preventNull(target);
        if(!sharedWaypoints.containsKey(player.getUniqueId())){
            sharedWaypoints.put(player.getUniqueId(), new HashMap<>());
        }
        if(!sharedWaypoints.get(player.getUniqueId()).isEmpty()) {
            List<Waypoint> temp = new ArrayList<>();
            temp.add(waypoint);
            sharedWaypoints.get(player.getUniqueId()).put(target.getUniqueId(), temp);
        }
        if(sharedWaypoints.get(player.getUniqueId()).containsKey(target.getUniqueId())){
            sharedWaypoints.get(player.getUniqueId()).get(target.getUniqueId()).add(waypoint);
        }
    }
    public static void shareWaypoint(Player player, Player target, String waypointName){
preventNull(player);
        preventNull(target);
        if(!sharedWaypoints.containsKey(player.getUniqueId())){
            sharedWaypoints.put(player.getUniqueId(), new HashMap<>());
        }
        if(!sharedWaypoints.get(player.getUniqueId()).isEmpty()) {
            List<Waypoint> temp = new ArrayList<>();
            temp.add(getWaypoint(player, waypointName));
            sharedWaypoints.get(player.getUniqueId()).put(target.getUniqueId(), temp);
        }
        if(sharedWaypoints.get(player.getUniqueId()).containsKey(target.getUniqueId())){
            sharedWaypoints.get(player.getUniqueId()).get(target.getUniqueId()).add(getWaypoint(player, waypointName));
        }
    }
    public static void addWaypoint(Player player, Waypoint waypoint){
        preventNull(player);
        waypoints.get(player.getUniqueId()).addWaypoint(waypoint);
        WaypointManager.saveAllWaypoints(InfinitumPlugin.getInstance());
    }
    public static void removeWaypoint(Player player, Waypoint waypoint){
        preventNull(player);
        waypoints.get(player.getUniqueId()).removeWaypoint(waypoint);
        WaypointManager.saveAllWaypoints(InfinitumPlugin.getInstance());
    }
    public static void removeWaypoint(Player player, String name){
        preventNull(player);
        waypoints.get(player.getUniqueId()).removeWaypoint(name);
        WaypointManager.saveAllWaypoints(InfinitumPlugin.getInstance());
    }
    public static void renameWaypoint(Player player, String name, String newName){
        preventNull(player);
        waypoints.get(player.getUniqueId()).renameWaypoint(name, newName);
        WaypointManager.saveAllWaypoints(InfinitumPlugin.getInstance());
    }
    public static void overrideWaypoint(Player player, String original, Waypoint waypoint){
        preventNull(player);
        waypoints.get(player.getUniqueId()).overrideWaypoint(original, waypoint);
        WaypointManager.saveAllWaypoints(InfinitumPlugin.getInstance());
    }
    public static void clearWaypoints(Player player){
        preventNull(player);
        waypoints.get(player.getUniqueId()).clearWaypoints();
        WaypointManager.saveAllWaypoints(InfinitumPlugin.getInstance());
    }
    public static boolean hasWaypoint(Player player, String name){
        preventNull(player);
        return waypoints.get(player.getUniqueId()).hasWaypoint(name);
    }
    public static void removePlayer(UUID uuid){
        if(waypoints.containsKey(uuid)){
            waypoints.remove(uuid);
            WaypointManager.saveAllWaypoints(InfinitumPlugin.getInstance());
        }
    }
    public static void saveAllWaypoints(InfinitumPlugin plugin) {
        JSONArray waypointsJsonArray = new JSONArray();
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        for (Map.Entry<UUID, WaypointList> entry : waypoints.entrySet()) {
            UUID playerUUID = entry.getKey();
            WaypointList waypointList = entry.getValue();

            JSONObject playerWaypointsJson = new JSONObject();
            playerWaypointsJson.put("playerUUID", playerUUID.toString());
            playerWaypointsJson.put("waypoints", waypointList.toJSONArray());
            waypointsJsonArray.add(playerWaypointsJson);
        }

        File waypointsFile = new File(plugin.getDataFolder(), "waypoints.json");

        try (FileWriter writer = new FileWriter(waypointsFile)) {
            writer.write(waypointsJsonArray.toJSONString());
            plugin.getLogger().info("Waypoints saved to file: " + waypointsFile.getAbsolutePath());
        } catch (IOException e) {
            plugin.getLogger().severe("Error saving waypoints to file: " + e.getMessage());
        }
    }
    public static void readAllWaypoints(InfinitumPlugin plugin) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        File waypointsFile = new File(plugin.getDataFolder(), "waypoints.json");

        if (!waypointsFile.exists()) {
            return;
        }

        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(waypointsFile)) {
            JSONArray waypointsJsonArray = (JSONArray) parser.parse(reader);

            for (Object object : waypointsJsonArray) {
                JSONObject playerWaypointsJson = (JSONObject) object;
                UUID playerUUID = UUID.fromString((String) playerWaypointsJson.get("playerUUID"));
                JSONArray waypointsArray = (JSONArray) playerWaypointsJson.get("waypoints");

                WaypointList waypointList = new WaypointList();

                for (Object waypointObject : waypointsArray) {
                    JSONObject waypointJson = (JSONObject) waypointObject;
                    String name = (String) waypointJson.get("name");
                    String worldName = (String) waypointJson.get("world");
                    double x = (double) waypointJson.get("x");
                    double y = (double) waypointJson.get("y");
                    double z = (double) waypointJson.get("z");

                    World world = Bukkit.getWorld(worldName);
                    if (world == null) {
                        plugin.getLogger().warning("World '" + worldName + "' for waypoint " + name + " not found. Skipping waypoint.");
                        continue;
                    }

                    Waypoint waypoint = new Waypoint(name, world, x, y, z);
                    waypointList.addWaypoint(waypoint);
                }

                waypoints.put(playerUUID, waypointList);
            }

            plugin.getLogger().info("Waypoints loaded from file: " + waypointsFile.getAbsolutePath());
        } catch (IOException | ParseException e) {
            plugin.getLogger().severe("Error reading waypoints from file: " + e.getMessage());
        }
    }
    public static void preventNull(Player player){
        if(waypoints.get(player.getUniqueId()) == null)
            waypoints.put(player.getUniqueId(), new WaypointList());
    }
}