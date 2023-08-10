package cn.crtlprototypestudios.infminecrafthelloplugin.managers;

import cn.crtlprototypestudios.infminecrafthelloplugin.InfMinecraftHelloPlugin;
import cn.crtlprototypestudios.infminecrafthelloplugin.classes.factions.Faction;
import cn.crtlprototypestudios.infminecrafthelloplugin.classes.factions.FactionPlayerInfo;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FactionsManager {
    private static List<Faction> factions = new ArrayList<>();
    public static boolean findPlayerInFaction(Player player) {
        if(factions.isEmpty()) return false;
        for (Faction faction : factions) {
            for (FactionPlayerInfo playerInfo : faction.getMembers()) {
                if (playerInfo.getUsername().equals(player.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
    public static FactionPlayerInfo getPlayerInFaction(Player player){
        if(factions.isEmpty()) return null;
        for (Faction faction : factions) {
            for (FactionPlayerInfo playerInfo : faction.getMembers()) {
                if (playerInfo.getUsername().equals(player.getName())) {
                    return playerInfo;
                }
            }
        }
        return null;
    }
    public static boolean isPlayerLeader(Player player){
        if(factions.isEmpty()) return false;
        for (Faction faction : factions) {
            for (FactionPlayerInfo playerInfo : faction.getMembers()) {
                if (playerInfo.getUsername().equals(player.getName())) {
                    return playerInfo.isLeader();
                }
            }
        }
        return false;
    }
    public static boolean isPlayerModerator(Player player){
        if(factions.isEmpty()) return false;
        for (Faction faction : factions) {
            for (FactionPlayerInfo playerInfo : faction.getMembers()) {
                if (playerInfo.getUsername().equals(player.getName())) {
                    return playerInfo.isModerator();
                }
            }
        }
        return false;
    }
    public static void addPlayerToFaction(Player player, Faction faction, boolean isMember, boolean isModerator, boolean isLeader){
        for (Faction faction1 : factions) {
            for (FactionPlayerInfo playerInfo : faction1.getMembers()) {
                if (playerInfo.getUsername().equals(player.getName())) {
                    playerInfo.setFaction(faction);
                    playerInfo.setMember(true);
                    playerInfo.setLeader(false);
                    playerInfo.setModerator(false);
                }
            }
        }
    }
    public static Faction addFaction(String factionName, ChatColor factionColor, Player leader) {
        Faction newFaction = new Faction(factionName, new FactionPlayerInfo(leader), factionColor);
        factions.add(newFaction);
        return newFaction;
    }
    public static void removeFaction(Faction faction){
        factions.remove(faction);
    }
    public static Faction getPlayerFaction(Player player){
        if(!findPlayerInFaction(player)) return null;
        for (Faction faction : factions) {
            for (FactionPlayerInfo playerInfo : faction.getMembers()) {
                if (playerInfo.getUUID().equals(player.getUniqueId())) {
                    if(!player.getName().equals(playerInfo.getUUID().toString())){
                        playerInfo.setPlayer(player);
                    }
                    return faction;
                }
            }
        }
        return null;
    }
    public static void updatePlayerInfos(){
        InfMinecraftHelloPlugin.getInstance().getServer().getOnlinePlayers().forEach(player -> {
            if(findPlayerInFaction(player)){
                getPlayerInFaction(player).setPlayer(player);
            }
        });
    }
    public static void writeToFile() {
        JSONArray factionsJsonArray = new JSONArray();

        for (Faction faction : factions) {
            factionsJsonArray.add(faction.toJSONObject());
        }

        // Get the plugin's data folder
        File dataFolder = InfMinecraftHelloPlugin.getInstance().getDataFolder();

        // Create a subdirectory if it doesn't exist
        File subdirectory = new File(dataFolder, "factions");
        subdirectory.mkdirs();

        // Write to the JSON file
        File jsonFile = new File(subdirectory, "factions.json");
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write(factionsJsonArray.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFromFile() {
        factions.clear(); // Clear the existing list of factions

        // Get the plugin's data folder
        File dataFolder = InfMinecraftHelloPlugin.getInstance().getDataFolder();

        // Read from the JSON file
        File jsonFile = new File(dataFolder, "factions/factions.json");
        if (jsonFile.exists()) {
            try (FileReader reader = new FileReader(jsonFile)) {
                JSONParser parser = new JSONParser();
                JSONArray factionsJsonArray = (JSONArray) parser.parse(reader);

                for (Object factionObject : factionsJsonArray) {
                    JSONObject factionJson = (JSONObject) factionObject;
                    Faction faction = new Faction(factionJson);
                    factions.add(faction);
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }
    public static List<Faction> getFactions() {
        return factions;
    }
}
