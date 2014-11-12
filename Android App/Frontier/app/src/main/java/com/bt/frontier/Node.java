package com.bt.frontier;

import org.json.JSONObject;

/**
 * Created by curtiskennington on 12/11/14.
 */
public class Node {

    private int id;
    private String name;
    private String owner;
    private double lat;
    private double lon;

    public Node(){

    }

    public Node(int id, String name, String owner, double lat, double lon) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.lat = lat;
        this.lon = lon;
    }

    public Node(JSONObject object){
        try{
            this.id = object.getInt("id");
            this.name = object.getString("name");
            this.owner = object.getString("owner");
            this.lat = object.getDouble("lat");
            this.lon = object.getDouble("long");
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
}
