package cn.crtlprototypestudios.infinitumplugin.classes.economy;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class EconomyVault {
    private String name = "";
    private UUID ownerUUID = null;
    private String ownerUsername = "";
    private int money = 0;
    private ArrayList<EconomyHistory> history = new ArrayList<EconomyHistory>();
    private EconomyVaultSettings settings;
    public EconomyVault(String name, String ownerUsername, UUID uuid, int money) {
        this.name = name;
        this.ownerUsername = ownerUsername;
        this.ownerUUID = uuid;
        this.money = money;
    }
    public EconomyVault(JSONObject object){
        this.name = (String) object.get("name");
        this.ownerUsername = (String) object.get("ownerUsername");
        this.ownerUUID = UUID.fromString((String) object.get("ownerUUID"));
        this.money = Integer.parseInt((String) object.get("money"));
        this.history = (ArrayList<EconomyHistory>) object.get("history");
        this.settings = new EconomyVaultSettings((JSONObject) object.get("settings"));
    }
    public String getName() {
        return this.name;
    }
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }
    public String getOwnerUsername() { return this.ownerUsername; }
    public int getMoney() {
        return this.money;
    }
    public ArrayList<EconomyHistory> getHistory() {
        return this.history;
    }
    public void setOwnerUsername(String username) {
        this.ownerUsername = username;
    }
    public void setOwnerUUID(UUID uuid) {
        this.ownerUUID = uuid;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    public void setHistory(ArrayList<EconomyHistory> history) {
        this.history = history;
    }
    public void addHistory(EconomyHistory history) {
        this.history.add(history);
    }
    public void removeHistory(EconomyHistory history) {
        this.history.remove(history);
    }
    public void removeHistory(int index) {
        this.history.remove(index);
    }
    public void clearHistory() {
        this.history.clear();
    }
    public void addMoney(int money) {
        this.money += money;
    }
    public void removeMoney(int money) {
        this.money -= money;
    }
    public JSONObject toJSONObject() {
        JSONObject object = new JSONObject();
        object.put("name", this.name);
        object.put("ownerUUID", this.ownerUUID.toString());
        object.put("ownerUsername", this.ownerUsername);
        object.put("money", this.money);
        object.put("history", this.history);
        object.put("settings", this.settings.toJSONObject());
        return object;
    }
}
