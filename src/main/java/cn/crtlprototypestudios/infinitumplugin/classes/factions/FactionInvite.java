package cn.crtlprototypestudios.infinitumplugin.classes.factions;

import org.bukkit.entity.Player;

import java.util.UUID;

public class FactionInvite {
    public FactionPlayerInfo sender;
    public Faction faction;
    public FactionInvite(FactionPlayerInfo sender, Faction faction) {
        this.sender = sender;
        this.faction = faction;
    }
    public FactionInvite(Player sender, Faction faction) {
        this.sender = new FactionPlayerInfo(sender);
        this.faction = faction;
    }
}
