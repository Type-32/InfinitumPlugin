package cn.crtlprototypestudios.infminecrafthelloplugin.classes.economy;

import org.json.simple.JSONObject;

public class EconomyVaultSettings {
    private int maxAmount = 1000000;
    private int compoundRate = 1;
    private boolean isPublic = false;
    private boolean doesCompound = true;
    public int getMaxAmount() {
        return maxAmount;
    }
    public int getCompoundRate() {
        return compoundRate;
    }
    public boolean isPublic() {
        return isPublic;
    }
    public boolean doesCompound() {
        return doesCompound;
    }
    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }
    public void setCompoundRate(int compoundRate) {
        this.compoundRate = compoundRate;
    }
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    public void setDoesCompound(boolean doesCompound) {
        this.doesCompound = doesCompound;
    }
    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("maxAmount", maxAmount);
        jsonObject.put("compoundRate", compoundRate);
        jsonObject.put("isPublic", isPublic);
        jsonObject.put("doesCompound", doesCompound);
        return jsonObject;
    }
    public EconomyVaultSettings(JSONObject jsonObject){
        this.maxAmount = Integer.parseInt(jsonObject.get("maxAmount").toString());
        this.compoundRate = Integer.parseInt(jsonObject.get("compoundRate").toString());
        this.isPublic = Boolean.parseBoolean(jsonObject.get("isPublic").toString());
        this.doesCompound = Boolean.parseBoolean(jsonObject.get("doesCompound").toString());
    }
    public EconomyVaultSettings(int maxAmount, int compoundRate, boolean isPublic, boolean doesCompound){
        this.maxAmount = maxAmount;
        this.compoundRate = compoundRate;
        this.isPublic = isPublic;
        this.doesCompound = doesCompound;
    }
    public EconomyVaultSettings(){
        this.maxAmount = 1000000;
        this.compoundRate = 1;
        this.isPublic = false;
        this.doesCompound = true;
    }
}
