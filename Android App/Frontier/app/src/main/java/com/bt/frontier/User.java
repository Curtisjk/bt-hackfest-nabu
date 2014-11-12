package com.bt.frontier;

import org.json.JSONObject;

public class User {


    private int id;
    private String name;
    private int level;
    private int xp;

    public User(JSONObject object){
        try{
            this.id = object.getInt("id");
            this.name = object.getString("name");
            this.level = object.getInt("level");
            this.xp = object.getInt("xp");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String toString(){
        return name + "- Level: "+level + " XP: "+xp;
    }
}
