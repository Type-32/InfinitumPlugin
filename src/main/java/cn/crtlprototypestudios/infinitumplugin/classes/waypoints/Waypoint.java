package cn.crtlprototypestudios.infinitumplugin.classes.waypoints;

import cn.crtlprototypestudios.infinitumplugin.classes.factions.Faction;
import org.bukkit.Location;
import org.bukkit.World;
import org.json.simple.JSONObject;

public class Waypoint {
    private String name;
    private World world;
    private double x, y, z;
    private boolean isShared = false, isFactionShared = false;
    private Faction sharedByFaction = null;
    public Waypoint(String name, World world, double x, double y, double z) {
        this.name = name;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Waypoint(String name, Location location) {
        this.name = name;
        this.world = location.getWorld();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }
    public Waypoint(Waypoint waypoint, boolean isShared){
        this.name = waypoint.getName();
        this.world = waypoint.getWorld();
        this.x = waypoint.getX();
        this.y = waypoint.getY();
        this.z = waypoint.getZ();
        this.isShared = isShared;
    }
    public Waypoint(Waypoint waypoint, boolean isFactionShared, Faction faction){
        this.name = waypoint.getName();
        this.world = waypoint.getWorld();
        this.x = waypoint.getX();
        this.y = waypoint.getY();
        this.z = waypoint.getZ();
        this.isFactionShared = isFactionShared;
        this.sharedByFaction = faction;
    }
    public Waypoint(JSONObject object){
        this.name = object.get("name").toString();
        this.world = org.bukkit.Bukkit.getWorld(object.get("world").toString());
        this.x = Double.parseDouble(object.get("x").toString());
        this.y = Double.parseDouble(object.get("y").toString());
        this.z = Double.parseDouble(object.get("z").toString());
        this.isShared = Boolean.parseBoolean(object.get("isShared").toString());
        this.isFactionShared = Boolean.parseBoolean(object.get("isFactionShared").toString());
        if(isFactionShared){
            this.sharedByFaction = new Faction((JSONObject) object.get("sharedByFaction"));
        }
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public boolean isShared() {
        return isShared;
    }
    public void setShared(boolean shared) {
        isShared = shared;
    }
    public boolean isFactionShared() {
        return isFactionShared;
    }
    public boolean setFactionShared(boolean factionShared) {
        isFactionShared = factionShared;
        return isFactionShared;
    }
    public Faction getSharedByFaction() {
        return sharedByFaction;
    }

    public void setXYZ(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setWorld(World world){
        this.world = world;
    }

    public void setName(String name){
        this.name = name;
    }

    public Location toLocation(){
        return new Location(world,x,y,z);
    }
    public JSONObject toJSONObject(){
        JSONObject temp = new JSONObject();
        temp.put("name",name);
        temp.put("world",world.getName());
        temp.put("x",x);
        temp.put("y",y);
        temp.put("z",z);
        temp.put("isShared",isShared);
        temp.put("isFactionShared",isFactionShared);
        if(isFactionShared){
            temp.put("sharedByFaction",sharedByFaction.toJSONObject());
        }
        return temp;
    }

    public String toString(){
        return String.format("%s in [%s] at [%f %f %f]",name,world.getName(),x,y,z);
    }

    public String posToString(boolean roundToNearestInt){
        return !roundToNearestInt ? String.format("%f %f %f",x,y,z) : String.format("%d %d %d",(int)x,(int)y,(int)z);
    }

    public boolean equals(Waypoint waypoint){
        return this.name.equals(waypoint.getName()) && this.world.equals(waypoint.getWorld()) && this.x == waypoint.getX() && this.y == waypoint.getY() && this.z == waypoint.getZ();
    }

    public void setLocation(Location location) {
        this.world = location.getWorld();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }
}
