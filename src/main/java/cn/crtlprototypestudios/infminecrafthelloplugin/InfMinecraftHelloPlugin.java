package cn.crtlprototypestudios.infminecrafthelloplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class InfMinecraftHelloPlugin extends JavaPlugin {

    private HashMap<UUID, UUID> tpaRequests = new HashMap<>();
    private HashMap<String, Location> playerHomes = new HashMap<>();
    private int maxHomesPerPlayer = 5;

    @Override
    public void onEnable() {
        // Plugin startup logic
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();

        // Store death location
        Location deathLoc = player.getLocation();
        DeathManager.setDeathLocation(player, deathLoc);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if(command.getName().equalsIgnoreCase("tpa")) {
            if(args.length != 1) {
                player.sendMessage(ChatColor.RED + "Usage: /tpa <player>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if(target == null) {
                player.sendMessage(ChatColor.RED + "That player is not online!");
                return true;
            }

            if(target.equals(player)) {
                player.sendMessage(ChatColor.RED + "You cannot teleport to yourself!");
                return true;
            }

            tpaRequests.put(target.getUniqueId(), player.getUniqueId());
            target.sendMessage(ChatColor.AQUA + player.getName() + " has requested to teleport to you.");
            target.sendMessage(ChatColor.AQUA + "To accept, use /tpaccept");
            player.sendMessage(ChatColor.AQUA + "Teleport request sent to " + target.getName() + ". Waiting for response...");
            return true;

        } else if(command.getName().equalsIgnoreCase("tpaccept")) {
            if (!tpaRequests.containsKey(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You have no pending requests!");
                return true;
            }

            Player requester = Bukkit.getPlayer(tpaRequests.get(player.getUniqueId()));

            if (requester == null) {
                player.sendMessage(ChatColor.RED + "That player is no longer online!");
                tpaRequests.remove(player.getUniqueId());
                return true;
            }

            requester.teleport(player);
            tpaRequests.remove(player.getUniqueId());

            player.sendMessage((ChatColor.AQUA + "Accepted teleport request from ") + (ChatColor.GOLD + requester.getName()));
            requester.sendMessage((ChatColor.GOLD + player.getName()) + (ChatColor.AQUA + " accepted your teleport request!"));

            return true;

        }else if(command.getName().equalsIgnoreCase("sethome")) {
            Player p = (Player) sender;
            if(args.length != 1) {
                p.sendMessage(ChatColor.GRAY + "/sethome <name>");
                return true;
            }

            String name = args[0];
            Location loc = p.getLocation();

            // Limit number of homes
            if(playerHomes.size() >= maxHomesPerPlayer) {
                p.sendMessage(ChatColor.RED + "You have reached the maximum amount of homes you can create.");
                return true;
            }

            playerHomes.put(name, loc);
            p.sendMessage((ChatColor.AQUA + "Created home ") + (ChatColor.GOLD + name));

            // /delhome
        } else if(command.getName().equalsIgnoreCase("delhome")) {
            Player p = (Player) sender;
            if(args.length != 1) {
                p.sendMessage(ChatColor.GRAY + "/delhome <name>");
                return true;
            }

            String name = args[0];
            playerHomes.remove(name);
            p.sendMessage((ChatColor.AQUA + "Deleted home ") + (ChatColor.GOLD + name));

            // /home
        } else if(command.getName().equalsIgnoreCase("home")) {
            Player p = (Player) sender;

            if(args.length > 1) {
                p.sendMessage(ChatColor.GRAY + "/home <home_name>");
                return true;
            }

            Location loc;
            if(args.length == 0) {
                // Go to default home
                loc = playerHomes.get("home");
            } else {
                // Go to named home
                String name = args[0];
                loc = playerHomes.get(name);
            }

            if(loc != null) {
                p.teleport(loc);
                p.sendMessage((ChatColor.GOLD + "Teleported you to home ") + (ChatColor.AQUA + args[0]) + (ChatColor.GOLD + "!"));
            } else {
                p.sendMessage((ChatColor.RED + "There is no home with the name ") + (ChatColor.GOLD + args[0]) + (ChatColor.RED + "."));
            }

            // /back - changed to use last death location
        } else if(command.getName().equalsIgnoreCase("back")) {
            Player p = (Player) sender;
            Location deathLoc = DeathManager.getDeathLocation(p); // Custom manager

            if(deathLoc != null) {
                p.teleport(deathLoc);
                p.sendMessage(ChatColor.AQUA + "Teleported to your death location.");
                DeathManager.clearDeathLocation(p);
            } else {
                p.sendMessage(ChatColor.RED + "There are no previous death positions to teleport.");
            }

            // /homesettings - ops only
        } else if(command.getName().equalsIgnoreCase("homesettings")) {
            if(!(sender instanceof Player)) return true;

            Player p = (Player) sender;
            if(!p.isOp()) {
                p.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                return true;
            }

            if(args.length != 1) {
                p.sendMessage(ChatColor.GRAY + "/homesettings <Max Amount of Homes>");
                return true;
            }

            // Set new maximium homes per player
            int max = Integer.parseInt(args[0]);
            maxHomesPerPlayer = max;

            p.sendMessage((ChatColor.AQUA + "Home limit is now ") + (ChatColor.GOLD + String.format("%s", max)));

        }
        return false;
    }
}