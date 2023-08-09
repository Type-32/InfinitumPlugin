package cn.crtlprototypestudios.infminecrafthelloplugin.classes.factions;

import cn.crtlprototypestudios.infminecrafthelloplugin.classes.economy.EconomyVault;
import org.bukkit.ChatColor;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Faction {
    private String name = "";
    private EconomyVault vault = null;
    private FactionPlayerInfo leader = null;
    private ArrayList<FactionPlayerInfo> moderators = new ArrayList<FactionPlayerInfo>();
    private ArrayList<FactionPlayerInfo> members = new ArrayList<FactionPlayerInfo>();
    private ArrayList<Faction> alliedFactions = new ArrayList<Faction>();
    private ArrayList<Faction> enemyFactions = new ArrayList<Faction>();
    private String prefix = "";
    private String suffix = "";
    private ChatColor color = ChatColor.WHITE;
    private ChatColor prefixColor = ChatColor.WHITE;
    private ChatColor suffixColor = ChatColor.WHITE;
    public FactionSettings factionSettings;
    public Faction(JSONObject factionJSON){
        this.name = (String) factionJSON.get("name");
        this.prefix = (String) factionJSON.get("prefix");
        this.suffix = (String) factionJSON.get("suffix");
        this.color = ChatColor.valueOf((String) factionJSON.get("color"));
        this.prefixColor = ChatColor.valueOf((String) factionJSON.get("prefixColor"));
        this.suffixColor = ChatColor.valueOf((String) factionJSON.get("suffixColor"));
        this.vault = new EconomyVault((JSONObject) factionJSON.get("vault"));
    }
    public Faction(String name, FactionPlayerInfo leader, ChatColor color){
        this.name = name;
        this.leader = leader;
        this.color = color;
    }
    public void setVault(int vault){
        this.vault.setMoney(vault);
    }
    public EconomyVault getVault(){
        return vault;
    }
    public void addVault(int amount){
        vault.addMoney(amount);
    }
    public void subtractVault(int amount){
        vault.removeMoney(amount);
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setLeader(FactionPlayerInfo leader){
        this.leader = leader;
    }
    public FactionPlayerInfo getLeader(){
        return leader;
    }
    public void setModerators(ArrayList<FactionPlayerInfo> moderators){
        this.moderators = moderators;
    }
    public ArrayList<FactionPlayerInfo> getModerators(){
        return moderators;
    }
    public void setMembers(ArrayList<FactionPlayerInfo> members){
        this.members = members;
    }
    public ArrayList<FactionPlayerInfo> getMembers(){
        return members;
    }
    public ArrayList<FactionPlayerInfo> getAllMembers(boolean excludeLeader){
        ArrayList<FactionPlayerInfo> allMembers = new ArrayList<FactionPlayerInfo>();
        if(!excludeLeader) allMembers.add(leader);
        allMembers.addAll(moderators);
        allMembers.addAll(members);
        return allMembers;
    }
    public void setAlliedFactions(ArrayList<Faction> alliedFactions){
        this.alliedFactions = alliedFactions;
    }
    public ArrayList<Faction> getAlliedFactions(){
        return alliedFactions;
    }
    public void setEnemyFactions(ArrayList<Faction> enemyFactions){
        this.enemyFactions = enemyFactions;
    }
    public ArrayList<Faction> getEnemyFactions(){
        return enemyFactions;
    }
    public void setPrefix(String prefix){
        this.prefix = prefix;
    }
    public String getPrefix(){
        return prefix;
    }
    public void setSuffix(String suffix){
        this.suffix = suffix;
    }
    public String getSuffix(){
        return suffix;
    }
    public void setColor(ChatColor color){
        this.color = color;
    }
    public ChatColor getColor(){
        return color;
    }
    public void setPrefixColor(ChatColor prefixColor){
        this.prefixColor = prefixColor;
    }
    public ChatColor getPrefixColor(){
        return prefixColor;
    }
    public void setSuffixColor(ChatColor suffixColor){
        this.suffixColor = suffixColor;
    }
    public ChatColor getSuffixColor(){
        return suffixColor;
    }
    public void addModerator(FactionPlayerInfo moderator){
        moderators.add(moderator);
    }
    public void removeModerator(FactionPlayerInfo moderator){
        moderators.remove(moderator);
    }
    public void addMember(FactionPlayerInfo member){
        members.add(member);
    }
    public void removeMember(FactionPlayerInfo member){
        members.remove(member);
    }
    public void addAlliedFaction(Faction faction){
        alliedFactions.add(faction);
    }
    public void removeAlliedFaction(Faction faction){
        alliedFactions.remove(faction);
    }
    public void addEnemyFaction(Faction faction){
        enemyFactions.add(faction);
    }
    public void removeEnemyFaction(Faction faction){
        enemyFactions.remove(faction);
    }
    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("leader", leader.toJSONObject());
        jsonObject.put("moderators", moderators);
        jsonObject.put("members", members);
        jsonObject.put("alliedFactions", alliedFactions);
        jsonObject.put("enemyFactions", enemyFactions);
        jsonObject.put("prefix", prefix);
        jsonObject.put("suffix", suffix);
        jsonObject.put("color", color);
        jsonObject.put("prefixColor", prefixColor);
        jsonObject.put("suffixColor", suffixColor);
        jsonObject.put("vault", vault.toJSONObject());
        jsonObject.put("settings", factionSettings.toJSONObject());
        return jsonObject;
    }
}
