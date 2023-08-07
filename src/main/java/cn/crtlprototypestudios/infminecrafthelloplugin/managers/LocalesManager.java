package cn.crtlprototypestudios.infminecrafthelloplugin.managers;

import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalesManager {
    public static ResourceBundle Locales = ResourceBundle.getBundle("messages");
    public static void fetchLocale(String locale){
        Locales = ResourceBundle.getBundle("messages", new Locale(locale));
    }
    public static void fetchPlayerLocale(Player player){
        Locales = ResourceBundle.getBundle("messages", Locale.forLanguageTag(player.getLocale()));
    }
}
