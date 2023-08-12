package cn.crtlprototypestudios.infinitumplugin.managers;

import cn.crtlprototypestudios.infinitumplugin.UTF8Control;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalesManager {
    public static ResourceBundle Locales = ResourceBundle.getBundle("messages");
    public static void fetchLocale(String locale){
        Locales = ResourceBundle.getBundle("messages", new Locale(locale), new UTF8Control());
    }
    public static void fetchPlayerLocale(Player player){
        Locales = ResourceBundle.getBundle("messages", Locale.forLanguageTag(player.getLocale()), new UTF8Control());
    }
    @NotNull
    @Nls
    public static String getProp(String key){
        return Locales.getString(key);
    }
    @NotNull
    @Nls
    public static String getPropFormatted(String key, Object... args){
        return String.format(Locales.getString(key), args);
    }
}
