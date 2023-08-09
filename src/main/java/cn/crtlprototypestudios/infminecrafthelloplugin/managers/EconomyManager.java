package cn.crtlprototypestudios.infminecrafthelloplugin.managers;

import cn.crtlprototypestudios.infminecrafthelloplugin.InfMinecraftHelloPlugin;
import cn.crtlprototypestudios.infminecrafthelloplugin.classes.economy.EconomyPlayer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EconomyManager {
    private static List<EconomyPlayer> players = new ArrayList<>();
    public static void writeToFile() {
        JSONArray playersJsonArray = new JSONArray();

        for (EconomyPlayer player : players) {
            playersJsonArray.add(player.toJSONObject());
        }

        // Get the plugin's data folder
        File dataFolder = InfMinecraftHelloPlugin.getInstance().getDataFolder();

        // Create a subdirectory if it doesn't exist
        File subdirectory = new File(dataFolder, "economy");
        subdirectory.mkdirs();

        // Write to the JSON file
        File jsonFile = new File(subdirectory, "economy_players.json");
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write(playersJsonArray.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFromFile() {
        players.clear(); // Clear the existing list of players

        // Get the plugin's data folder
        File dataFolder = InfMinecraftHelloPlugin.getInstance().getDataFolder();

        // Read from the JSON file
        File jsonFile = new File(dataFolder, "economy/economy_players.json");
        if (jsonFile.exists()) {
            try (FileReader reader = new FileReader(jsonFile)) {
                JSONParser parser = new JSONParser();
                JSONArray playersJsonArray = (JSONArray) parser.parse(reader);

                for (Object playerObject : playersJsonArray) {
                    JSONObject playerJson = (JSONObject) playerObject;
                    EconomyPlayer player = new EconomyPlayer(playerJson);
                    players.add(player);
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
