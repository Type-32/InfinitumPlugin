package cn.crtlprototypestudios.infinitumplugin.managers;

import cn.crtlprototypestudios.infinitumplugin.InfinitumPlugin;
import cn.crtlprototypestudios.infinitumplugin.classes.factions.Faction;
import cn.crtlprototypestudios.infinitumplugin.classes.factions.FactionInvite;
import cn.crtlprototypestudios.infinitumplugin.classes.factions.FactionPlayerInfo;
import org.apache.commons.lang3.RandomStringUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FactionsManager {
    /**
     * This class is responsible for managing factions.
     * It stores a list of factions, and provides methods for creating, deleting, and modifying factions.
     * It also provides methods for getting a player's faction, and checking if a player is in a faction.
     */

    private static List<Faction> factions = new ArrayList<>();
    public static HashMap<UUID, List<FactionInvite>> factionInvites = new HashMap<>();

    public static FactionInvite findInvite(String factionId){
        if(factionInvites.isEmpty()) return null;
        for (UUID uuid : factionInvites.keySet()) {
            for (FactionInvite factionInvite : factionInvites.get(uuid)) {
                if(factionInvite.faction.getFactionId().equals(factionId)){
                    return factionInvite;
                }
            }
        }
        return null;
    }
    public static FactionInvite findInvite(Player player){
        if(factionInvites.isEmpty()) return null;
        for (UUID uuid : factionInvites.keySet()) {
            for (FactionInvite factionInvite : factionInvites.get(uuid)) {
                if(factionInvite.sender.getUUID().equals(player.getUniqueId())){
                    return factionInvite;
                }
            }
        }
        return null;
    }
    public static boolean isMemberInFaction(Player player, Faction faction){
        if(factions.isEmpty()) return false;
        for (FactionPlayerInfo playerInfo : faction.getMembers()) {
            if(playerInfo.getUUID().equals(player.getUniqueId())){
                return true;
            }
        }
        return false;
    }
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
    public static boolean findPlayerInFaction(UUID uuid){
        if(factions.isEmpty()) return false;
        for (Faction faction : factions) {
            for (FactionPlayerInfo playerInfo : faction.getMembers()) {
                if (playerInfo.getUUID().equals(uuid)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static String getFactionRuleValue(Player player, String rule){
        if(factions.isEmpty()) return null;
        for (Faction faction : factions) {
            for (FactionPlayerInfo playerInfo : faction.getMembers()) {
                if (playerInfo.getUsername().equals(player.getName())) {
                    return faction.factionSettings.getSettingsValue(rule).toString();
                }
            }
        }
        return null;
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
    public static Player getPlayer(FactionPlayerInfo p){
        return InfinitumPlugin.getInstance().getServer().getPlayer(p.getUUID());
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
    public static boolean isFactionNull(Player player){
        if(factions.isEmpty()) return true;
        for (Faction faction : factions) {
            for (FactionPlayerInfo playerInfo : faction.getMembers()) {
                if (playerInfo.getUsername().equals(player.getName())) {
                    return faction == null;
                }
            }
        }
        return true;
    }
    public static boolean isFactionNull(Faction faction){
        return faction == null;
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
    public static Faction getPlayerFaction(UUID uuid){
        if(!findPlayerInFaction(uuid)) return null;
        for (Faction faction : factions) {
            for (FactionPlayerInfo playerInfo : faction.getMembers()) {
                if (playerInfo.getUUID().equals(uuid)) {
                    return faction;
                }
            }
        }
        return null;
    }
    public static void updatePlayerInfos(){
        InfinitumPlugin.getInstance().getServer().getOnlinePlayers().forEach(player -> {
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
        File dataFolder = InfinitumPlugin.getInstance().getDataFolder();

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
        File dataFolder = InfinitumPlugin.getInstance().getDataFolder();

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

    public static boolean factionExists(String factionName) {
        for (Faction faction : factions) {
            if (faction.getName().equalsIgnoreCase(factionName)) {
                return true;
            }
        }
        return false;
    }

    public static void createFaction(Player player, String factionName) {
        Faction faction = new Faction(factionName, new FactionPlayerInfo(player), ChatColor.WHITE);
        factions.add(faction);
    }

    public static Faction getFaction(String factionName) {
        for (Faction faction : factions) {
            if (faction.getName().equalsIgnoreCase(factionName)) {
                return faction;
            }
        }
        return null;
    }

    public static void joinFaction(Player player, String factionName) {
        Faction faction = getFaction(factionName);
        if (faction != null) {
            faction.addMember(new FactionPlayerInfo(player));
        }
        throw new IllegalArgumentException("Faction does not exist");
    }

    public static void leaveFaction(Player player) {
        Faction faction = getPlayerFaction(player);
        if (faction != null) {
            faction.removeMember(player, true);
        }
    }

    public static void kickPlayerFromFaction(Player target) {
        Faction faction = getPlayerFaction(target);
        if (faction != null) {
            faction.removeMember(target, false);
        }
    }

    public static void promotePlayer(Player target) {
        Faction faction = getPlayerFaction(target);
        if (faction != null) {
            faction.promoteMember(target);
        }
    }

    public static void demotePlayer(Player target) {
        Faction faction = getPlayerFaction(target);
        if (faction != null) {
            faction.demoteMember(target);
        }
    }
    public static boolean isSameFaction(Player player1, Player player2){
        if(!findPlayerInFaction(player1) || !findPlayerInFaction(player2)) return false;
        return getPlayerFaction(player1).equals(getPlayerFaction(player2));
    }

    public static boolean isSameFaction(Player player, Faction faction){
        if(!findPlayerInFaction(player)) return false;
        return getPlayerFaction(player).equals(faction);
    }

    public static boolean isSameFaction(Faction faction, Player player){
        if(!findPlayerInFaction(player)) return false;
        return getPlayerFaction(player).equals(faction);
    }

    public static boolean isSameFaction(Faction faction1, Faction faction2){
        return faction1.equals(faction2);
    }

    public static void disbandFaction(Player player) {
        Faction faction = getPlayerFaction(player);
        if (faction != null) {
            faction.disband();
        }
    }

    public static void setFactionPrefix(Player player, String prefix) {
        Faction faction = getPlayerFaction(player);
        if (faction != null) {
            faction.setPrefix(prefix);
            updatePlayerNicknames();
        }

    }
    public static void setFactionSuffix(Player player, String suffix){
        Faction faction = getPlayerFaction(player);
        if (faction != null) {
            faction.setSuffix(suffix);
            updatePlayerNicknames();
        }
    }
    public static void updatePlayerNicknames(){
        for (Player player : InfinitumPlugin.getInstance().getServer().getOnlinePlayers()) {
            Faction faction = getPlayerFaction(player);
            if (faction != null) {
                player.setDisplayName((faction.getPrefixColor() + "[" + faction.getPrefix() + "] ") + (faction.getColor() + player.getName()) + (faction.getSuffixColor() + " <" + faction.getSuffix() + ">"));
            }
        }
    }

    public static void setFactionColor(Player player, String color) {
        Faction faction = getPlayerFaction(player);
        if (faction != null) {
            faction.setColor(ChatColor.valueOf(color));
            updatePlayerNicknames();
        }
    }
    public static void setFactionPrefixColor(Player player, String color){
        Faction faction = getPlayerFaction(player);
        if (faction != null) {
            faction.setPrefixColor(ChatColor.valueOf(color));
            updatePlayerNicknames();
        }
    }
    public static void setFactionSuffixColor(Player player, String color){
        Faction faction = getPlayerFaction(player);
        if (faction != null) {
            faction.setSuffixColor(ChatColor.valueOf(color));
            updatePlayerNicknames();
        }
    }

    public static void setFactionRules(Player player, String rules, Object value) {
        Faction faction = getPlayerFaction(player);
        if (faction != null) {
            faction.factionSettings.setSettingsValue(rules,value);
        }
    }

    public static String generateFactionId() {
        String id = UUID.randomUUID().toString() + RandomStringUtils.randomAlphanumeric(5);
        while (factionExists(id)) {
            id = UUID.randomUUID().toString() + RandomStringUtils.randomAlphanumeric(5);
        }
        return id;
    }

    public static void allyFactions(Player player, Faction faction) {
        Faction playerFaction = getPlayerFaction(player);
        if (playerFaction != null) {
            playerFaction.addAlliedFaction(faction);
        }
    }
    public static void unallyFactions(Player player, Faction faction){
        Faction playerFaction = getPlayerFaction(player);
        if (playerFaction != null) {
            playerFaction.removeAlliedFaction(faction);
        }
    }
    public static void enemyFactions(Player player, Faction faction){
        Faction playerFaction = getPlayerFaction(player);
        if (playerFaction != null) {
            playerFaction.addEnemyFaction(faction);
        }
    }
    public static void unenemyFactions(Player player, Faction faction){
        Faction playerFaction = getPlayerFaction(player);
        if (playerFaction != null) {
            playerFaction.removeEnemyFaction(faction);
        }
    }
}
