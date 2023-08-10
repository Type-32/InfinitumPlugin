package cn.crtlprototypestudios.infinitumplugin.classes.economy;

import org.json.simple.JSONObject;

public class EconomyHistory {
    private int amount;
    private EconomyAction action;
    private EconomyActionEntityType fromType;
    private EconomyActionEntityType toType;
    public EconomyHistory(int amount, EconomyAction action, EconomyActionEntityType fromType, EconomyActionEntityType toType) {
        this.amount = amount;
        this.action = action;
        this.fromType = fromType;
        this.toType = toType;
    }
    public EconomyHistory(JSONObject object){
        this.amount = Integer.parseInt(object.get("amount").toString());
        this.action = EconomyAction.valueOf(object.get("action").toString());
        this.fromType = EconomyActionEntityType.valueOf(object.get("fromType").toString());
        this.toType = EconomyActionEntityType.valueOf(object.get("toType").toString());
    }
    public int getAmount() {
        return amount;
    }
    public EconomyAction getAction() {
        return action;
    }
    public EconomyActionEntityType getFromType() {
        return fromType;
    }
    public EconomyActionEntityType getToType() {
        return toType;
    }
    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", this.amount);
        jsonObject.put("action", this.action.toString());
        jsonObject.put("fromType", this.fromType.toString());
        jsonObject.put("toType", this.toType.toString());
        return jsonObject;
    }
}
