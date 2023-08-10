package cn.crtlprototypestudios.infinitumplugin.classes;

import java.util.ArrayList;

public class SettingsCategory {
    public String category = "";
    public ArrayList<CategoryRules> rules = new ArrayList<>();
    public SettingsCategory(String categoryLabel){
        category = categoryLabel;
    }
    public SettingsCategory(String categoryLabel, ArrayList<CategoryRules> rules){
        category = categoryLabel;
        this.rules = rules;
    }
}
