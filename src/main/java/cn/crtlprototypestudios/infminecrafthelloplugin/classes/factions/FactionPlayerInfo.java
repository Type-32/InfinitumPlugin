package cn.crtlprototypestudios.infminecrafthelloplugin.classes.factions;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class FactionPlayerInfo {
    private String username = "";
    private UUID uuid = null;
    private int userLevel = 0;
    private boolean isLeader = false;
    private boolean isModerator = false;
    private boolean isMember = false;
    private Faction faction = null;
    private ArrayList<Faction> alliedFactions = new ArrayList<Faction>();
    private ArrayList<Faction> enemyFactions = new ArrayList<Faction>();
    public FactionPlayerInfo(Player player){
        this.username = player.getName();
        this.uuid = player.getUniqueId();
    }
    public void setPlayer(Player player){
        this.username = player.getName();
        this.uuid = player.getUniqueId();
    }
    public UUID getUUID(){
        return uuid;
    }
    public String getUsername(){
        return username;
    }
    public void setFaction(Faction faction){
        this.faction = faction;
    }
    public Faction getFaction(){
        return faction;
    }
    public boolean hasFaction(){
        return faction != null;
    }
    public void setLeader(boolean isLeader){
        this.isLeader = isLeader;
    }
    public boolean isLeader(){
        return isLeader;
    }
    public void setModerator(boolean isModerator){
        this.isModerator = isModerator;
    }
    public boolean isModerator(){
        return isModerator;
    }
    public void setMember(boolean isMember){
        this.isMember = isMember;
    }
    public boolean isMember(){
        return isMember;
    }
    public void setUserLevel(int userLevel){
        this.userLevel = userLevel;
    }
    public int getUserLevel(){
        return userLevel;
    }
    public void setAlliedFactions(ArrayList<Faction> alliedFactions){
        this.alliedFactions = alliedFactions;
    }
    public ArrayList<Faction> getAlliedFactions(){
        return alliedFactions;
    }
    public void addAlliedFaction(Faction faction){
        alliedFactions.add(faction);
    }
    public void removeAlliedFaction(Faction faction){
        alliedFactions.remove(faction);
    }
    public void setEnemyFactions(ArrayList<Faction> enemyFactions){
        this.enemyFactions = enemyFactions;
    }
    public ArrayList<Faction> getEnemyFactions(){
        return enemyFactions;
    }
    public void addEnemyFaction(Faction faction){
        enemyFactions.add(faction);
    }
    public void removeEnemyFaction(Faction faction){
        enemyFactions.remove(faction);
    }
    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("uuid", uuid.toString());
        jsonObject.put("userLevel", userLevel);
        jsonObject.put("isLeader", isLeader);
        jsonObject.put("isModerator", isModerator);
        jsonObject.put("isMember", isMember);
        jsonObject.put("faction", faction.toJSONObject());
        return jsonObject;
    }
}
