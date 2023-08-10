package cn.crtlprototypestudios.infinitumplugin.classes.waypoints;

import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class WaypointList {
    private List<Waypoint> values = new ArrayList<Waypoint>();

    public WaypointList(List<Waypoint> values){
        this.values = values;
    }
    public WaypointList(){
        values = new ArrayList<Waypoint>();
    }

    public List<Waypoint> getValues(){
        return values;
    }

    public void setValues(List<Waypoint> values){
        this.values = values;
    }

    public void addWaypoint(Waypoint waypoint){
        values.add(waypoint);
    }

    public void removeWaypoint(Waypoint waypoint){
        values.remove(waypoint);
    }

    public void removeWaypoint(String name){
        for(Waypoint waypoint : values){
            if(waypoint.getName().equals(name)){
                values.remove(waypoint);
            }
        }
    }

    public void clearWaypoints(){
        values.clear();
    }

    public void renameWaypoint(Waypoint waypoint, String name){
        for(Waypoint wp : values){
            if(wp.equals(waypoint)){
                wp.setName(name);
            }
        }
    }
    public void renameWaypoint(String old, String name){
        for(Waypoint wp : values){
            if(wp.getName().equals(old)){
                wp.setName(name);
            }
        }
    }

    public JSONArray toJSONArray(){
        JSONArray temp = new JSONArray();
        for(Waypoint waypoint : values){
            temp.add(waypoint.toJSONObject());
        }
        return temp;
    }

    public Waypoint getWaypoint(String name) {
        for(Waypoint waypoint : values){
            if(waypoint.getName().equals(name)){
                return waypoint;
            }
        }
        return null;
    }

    public void setWaypoint(Waypoint waypoint) {
        for(Waypoint wp : values){
            if(wp.getName().equals(waypoint.getName())){
                wp.setWorld(waypoint.getWorld());
                wp.setXYZ(waypoint.getX(),waypoint.getY(),waypoint.getZ());
            }
        }
    }

    public boolean isEmpty(){
        return values.isEmpty();
    }

    public boolean hasWaypoint(String name) {
        for(Waypoint waypoint : values){
            if(waypoint.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public void overrideWaypoint(String original, Waypoint waypoint) {
        for(Waypoint wp : values){
            if(wp.getName().equals(original)){
                wp.setWorld(waypoint.getWorld());
                wp.setXYZ(waypoint.getX(),waypoint.getY(),waypoint.getZ());
            }
        }
    }
}
