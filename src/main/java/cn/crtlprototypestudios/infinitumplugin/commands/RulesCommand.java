package cn.crtlprototypestudios.infinitumplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

public class RulesCommand implements CommandExecutor, TabCompleter {

    private final Dictionary<String, String> rulesSet = new Hashtable<>();

    public RulesCommand() {
        // Add your rules to the rulesSet Dictionary here.
        // Example:
        rulesSet.put("maxWaypointAmount", "10");
        rulesSet.put("rule2", "false");
        rulesSet.put("rule3", "5");
        rulesSet.put("rule4", "10");
        // ...
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // ... Your existing command handling code ...
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            // Provide suggestions for the first argument ("/rules <arg>")
            List<String> suggestions = new ArrayList<>();
            Enumeration<String> ruleName = rulesSet.keys();
            while(ruleName.hasMoreElements()){
                suggestions.add(ruleName.nextElement());
            }
            return suggestions;
        } else if (args.length == 2) {
            // Provide suggestions for the second argument ("/rules <Rule Name> <arg>")
            String ruleName = args[0].toLowerCase();
            if (rulesSet.get(ruleName) != null) {
                // Rule exists in the rulesSet, check its value type
                String ruleValue = rulesSet.get(ruleName);
                if (ruleValue.equalsIgnoreCase("true") || ruleValue.equalsIgnoreCase("false")) {
                    // Rule value is boolean, suggest "true" or "false"
                    List<String> suggestions = new ArrayList<>();
                    suggestions.add("true");
                    suggestions.add("false");
                    return suggestions;
                } else if (isInteger(ruleValue)) {
                    // Rule value is an integer, suggest integers
                    // You can provide a range of integers based on your requirements.
                    List<String> suggestions = new ArrayList<>();
                    suggestions.add("1");
                    suggestions.add("5");
                    suggestions.add("10");
                    // Add more integers as needed.
                    return suggestions;
                }
            }
        }

        // No suggestions for other arguments
        return null;
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}