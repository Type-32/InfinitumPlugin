package cn.crtlprototypestudios.infminecrafthelloplugin.classes.economy;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class EconomyPlayer {
    private String username = "";
    private UUID uuid = null;
    private int reknown = 100;
    private int money = 0;
    private int maxVault = 2;
    private ArrayList<EconomyVault> vaults = new ArrayList<EconomyVault>();
    private ArrayList<EconomyHistory> history = new ArrayList<EconomyHistory>();
    public EconomyPlayerSettings settings = new EconomyPlayerSettings();
    public EconomyPlayer(String username, UUID uuid, int reknown, int money) {
        this.username = username;
        this.uuid = uuid;
        this.reknown = reknown;
        this.money = money;
    }
    public EconomyPlayer(JSONObject object){
        this.username = object.get("username").toString();
        this.uuid = UUID.fromString(object.get("uuid").toString());
        this.reknown = Integer.parseInt(object.get("reknown").toString());
        this.money = Integer.parseInt(object.get("money").toString());
        for (Object historyObject : (ArrayList<Object>) object.get("history")) {
            this.history.add(new EconomyHistory((JSONObject) historyObject));
        }
        for (Object vaultObject : (ArrayList<Object>) object.get("vaults")) {
            this.vaults.add(new EconomyVault((JSONObject) vaultObject));
        }
        this.settings = new EconomyPlayerSettings((JSONObject) object.get("settings"));
    }
    public String getUsername() {
        return this.username;
    }
    public UUID getUUID() {
        return this.uuid;
    }
    public int getReknown() {
        return this.reknown;
    }
    public int getMoney() {
        return this.money;
    }
    public ArrayList<EconomyHistory> getHistory() {
        return this.history;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }
    public void setReknown(int reknown) {
        this.reknown = reknown;
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
    public void addReknown(int reknown) {
        this.reknown += reknown;
    }
    public void removeReknown(int reknown) {
        this.reknown -= reknown;
    }
    public void addVault(EconomyVault vault) {
        this.vaults.add(vault);
    }
    public void removeVault(EconomyVault vault) {
        this.vaults.remove(vault);
    }
    public void removeVault(int index) {
        this.vaults.remove(index);
    }
    public void clearVault() {
        this.vaults.clear();
    }
    public void setVaults(ArrayList<EconomyVault> vaults) {
        this.vaults = vaults;
    }
    public ArrayList<EconomyVault> getVaults() {
        return this.vaults;
    }
    public EconomyVault getVault(int index) {
        return this.vaults.get(index);
    }
    public EconomyVault getVault(String name) {
        for (EconomyVault vault : this.vaults) {
            if (vault.getName().equals(name)) {
                return vault;
            }
        }
        return null;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("username", this.username);
        json.put("uuid", this.uuid.toString());
        json.put("reknown", this.reknown);
        json.put("money", this.money);
        return json;
    }
}
