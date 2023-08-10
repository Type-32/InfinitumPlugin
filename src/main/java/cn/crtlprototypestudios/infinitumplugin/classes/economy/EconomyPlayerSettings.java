package cn.crtlprototypestudios.infinitumplugin.classes.economy;

import org.json.simple.JSONObject;

public class EconomyPlayerSettings {
    private int maxVaults = 2;
    private int maxAmount = 1000000;
    private boolean ignoresReknown = false;
    private boolean isPublic = false;

    public int getMaxVaults() {
        return maxVaults;
    }
    public int getMaxAmount() {
        return maxAmount;
    }
    public boolean ignoresReknown() {
        return ignoresReknown;
    }
    public boolean isPublic() {
        return isPublic;
    }
    public void setMaxVaults(int maxVaults) {
        this.maxVaults = maxVaults;
    }
    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }
    public void setIgnoresReknown(boolean ignoresReknown) {
        this.ignoresReknown = ignoresReknown;
    }
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("maxVaults", maxVaults);
        jsonObject.put("maxAmount", maxAmount);
        jsonObject.put("ignoresReknown", ignoresReknown);
        jsonObject.put("isPublic", isPublic);
        return jsonObject;
    }
    public EconomyPlayerSettings(JSONObject jsonObject){
        this.maxVaults = Integer.parseInt(jsonObject.get("maxVaults").toString());
        this.maxAmount = Integer.parseInt(jsonObject.get("maxAmount").toString());
        this.ignoresReknown = Boolean.parseBoolean(jsonObject.get("ignoresReknown").toString());
        this.isPublic = Boolean.parseBoolean(jsonObject.get("isPublic").toString());
    }
    public EconomyPlayerSettings(int maxVaults, int maxAmount, boolean ignoresReknown, boolean isPublic){
        this.maxVaults = maxVaults;
        this.maxAmount = maxAmount;
        this.ignoresReknown = ignoresReknown;
        this.isPublic = isPublic;
    }
    public EconomyPlayerSettings(){
        this.maxVaults = 2;
        this.maxAmount = 1000000;
        this.ignoresReknown = false;
        this.isPublic = false;
    }
}
