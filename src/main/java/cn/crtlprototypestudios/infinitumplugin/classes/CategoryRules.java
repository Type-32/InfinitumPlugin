package cn.crtlprototypestudios.infinitumplugin.classes;

public class CategoryRules {
    public String ruleLabel = "";
    public RuleInputType inputType = RuleInputType.Integer;
    public CategoryRules(String ruleLabel){
        this.ruleLabel = ruleLabel;
    }
    public CategoryRules(String ruleLabel, RuleInputType inputType){
        this.ruleLabel = ruleLabel;
        this.inputType = inputType;
    }
}
