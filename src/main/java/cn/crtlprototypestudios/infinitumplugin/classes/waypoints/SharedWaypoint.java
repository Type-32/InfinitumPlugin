package cn.crtlprototypestudios.infinitumplugin.classes.waypoints;

import org.bukkit.World;
import org.json.simple.JSONObject;

import java.util.UUID;

public class SharedWaypoint extends Waypoint{

    private UUID sharingPlayer;
    public UUID getSharingPlayer(){
        return sharingPlayer;
    }
    public void setSharingPlayer(UUID sharingPlayer){
        this.sharingPlayer = sharingPlayer;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject obj = super.toJSONObject();
        obj.put("sharingPlayer",sharingPlayer.toString());
        return obj;
    }

    public SharedWaypoint(String name, World world, double x, double y, double z, UUID sharingPlayer) {
        super(name, world, x, y, z);
        sharingPlayer = sharingPlayer;
    }
    public SharedWaypoint(JSONObject object){
        super(object);
        sharingPlayer = UUID.fromString(object.get("sharingPlayer").toString());
    }
    public SharedWaypoint(Waypoint waypoint, UUID sharingPlayer){
        super(waypoint);
        this.sharingPlayer = sharingPlayer;
    }
}
