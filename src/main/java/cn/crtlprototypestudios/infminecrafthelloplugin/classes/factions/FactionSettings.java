package cn.crtlprototypestudios.infminecrafthelloplugin.classes.factions;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FactionSettings {
    public boolean membersPublic = false;
    public boolean moderatorsPublic = false;
    public boolean leadersPublic = false;
    public boolean alliedFactionsPublic = false;
    public boolean enemyFactionsPublic = false;
    public boolean factionPublic = true;
    public boolean allowFreeJoin = false;
    public boolean allowFreeLeave = false;
    public int maxMembers = 10;
    public int maxModerators = 3;
    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("membersPublic", membersPublic);
        jsonObject.put("moderatorsPublic", moderatorsPublic);
        jsonObject.put("leadersPublic", leadersPublic);
        jsonObject.put("alliedFactionsPublic", alliedFactionsPublic);
        jsonObject.put("enemyFactionsPublic", enemyFactionsPublic);
        jsonObject.put("factionPublic", factionPublic);
        jsonObject.put("allowFreeJoin", allowFreeJoin);
        jsonObject.put("allowFreeLeave", allowFreeLeave);
        jsonObject.put("maxMembers", maxMembers);
        jsonObject.put("maxModerators", maxModerators);
        return jsonObject;
    }
    public FactionSettings(JSONObject jsonObject){
        this.membersPublic = Boolean.parseBoolean(jsonObject.get("membersPublic").toString());
        this.moderatorsPublic = Boolean.parseBoolean(jsonObject.get("moderatorsPublic").toString());
        this.leadersPublic = Boolean.parseBoolean(jsonObject.get("leadersPublic").toString());
        this.alliedFactionsPublic = Boolean.parseBoolean(jsonObject.get("alliedFactionsPublic").toString());
        this.enemyFactionsPublic = Boolean.parseBoolean(jsonObject.get("enemyFactionsPublic").toString());
        this.factionPublic = Boolean.parseBoolean(jsonObject.get("factionPublic").toString());
        this.allowFreeJoin = Boolean.parseBoolean(jsonObject.get("allowFreeJoin").toString());
        this.allowFreeLeave = Boolean.parseBoolean(jsonObject.get("allowFreeLeave").toString());
        this.maxMembers = Integer.parseInt(jsonObject.get("maxMembers").toString());
        this.maxModerators = Integer.parseInt(jsonObject.get("maxModerators").toString());
    }
    public FactionSettings(boolean membersPublic, boolean moderatorsPublic, boolean leadersPublic, boolean alliedFactionsPublic, boolean enemyFactionsPublic, boolean factionPublic, boolean allowFreeJoin, boolean allowFreeLeave, int maxMembers, int maxModerators){
        this.membersPublic = membersPublic;
        this.moderatorsPublic = moderatorsPublic;
        this.leadersPublic = leadersPublic;
        this.alliedFactionsPublic = alliedFactionsPublic;
        this.enemyFactionsPublic = enemyFactionsPublic;
        this.factionPublic = factionPublic;
        this.allowFreeJoin = allowFreeJoin;
        this.allowFreeLeave = allowFreeLeave;
        this.maxMembers = maxMembers;
        this.maxModerators = maxModerators;
    }
    public FactionSettings(){
        this.membersPublic = false;
        this.moderatorsPublic = false;
        this.leadersPublic = false;
        this.alliedFactionsPublic = false;
        this.enemyFactionsPublic = false;
        this.factionPublic = true;
        this.allowFreeJoin = false;
        this.allowFreeLeave = false;
        this.maxMembers = 10;
        this.maxModerators = 3;
    }
    public void setSettingsValue(String key, Object value){
        switch(key){
            case "membersPublic":
                this.membersPublic = Boolean.parseBoolean(value.toString());
                break;
            case "moderatorsPublic":
                this.moderatorsPublic = Boolean.parseBoolean(value.toString());
                break;
            case "leadersPublic":
                this.leadersPublic = Boolean.parseBoolean(value.toString());
                break;
            case "alliedFactionsPublic":
                this.alliedFactionsPublic = Boolean.parseBoolean(value.toString());
                break;
            case "enemyFactionsPublic":
                this.enemyFactionsPublic = Boolean.parseBoolean(value.toString());
                break;
            case "factionPublic":
                this.factionPublic = Boolean.parseBoolean(value.toString());
                break;
            case "allowFreeJoin":
                this.allowFreeJoin = Boolean.parseBoolean(value.toString());
                break;
            case "allowFreeLeave":
                this.allowFreeLeave = Boolean.parseBoolean(value.toString());
                break;
            case "maxMembers":
                this.maxMembers = Integer.parseInt(value.toString());
                break;
            case "maxModerators":
                this.maxModerators = Integer.parseInt(value.toString());
                break;
        }
    }
    public static List<String> getRulesKey(){
        //Java 8
        List<String> keys = new ArrayList<>();
        keys.add("membersPublic");
        keys.add("moderatorsPublic");
        keys.add("leadersPublic");
        keys.add("alliedFactionsPublic");
        keys.add("enemyFactionsPublic");
        keys.add("factionPublic");
        keys.add("allowFreeJoin");
        keys.add("allowFreeLeave");
        keys.add("maxMembers");
        keys.add("maxModerators");
        return keys;
    }
}
