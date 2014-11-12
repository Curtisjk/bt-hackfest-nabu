package com.bt.frontier;

import org.json.JSONObject;

/**
 * Created by curtiskennington on 12/11/14.
 */
public class Node {

    private int level;
    private int id;
    private String name;
    private String owner;
    private double lat;
    private double lon;
    private int faction;

    public Node(){}

    public Node(JSONObject object){
        try{
            this.id = object.getInt("id");
            this.name = object.getString("name");
            this.owner = object.getString("owner");
            this.lat = object.getDouble("lat");
            this.lon = object.getDouble("long");
            this.level = object.getInt("level");
            this.faction = object.getInt("faction");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFaction() {
        return faction;
    }

    public void setFaction(int faction) {
        this.faction = faction;
    }

    public String toString(){
        return name + " - Level: "+level;
    }
}
