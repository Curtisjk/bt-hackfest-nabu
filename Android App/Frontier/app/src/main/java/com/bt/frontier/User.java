package com.bt.frontier;

import org.json.JSONObject;

public class User {

    private int id;
    private String openId;
    private String name;
    private int level;
    private int xp;

    public User(JSONObject object){
        try{
            this.id = object.getInt("id");
            this.openId = object.getString("openid");
            this.name = object.getString("name");
            this.level = object.getInt("level");
            this.xp = object.getInt("xp");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public User(String openId){
        this.openId = openId;
    }

    public int getId(){
        return this.id;
    }

    public String getOpenId(){
        return this.openId;
    }

    public String toString(){
        return name + "- Level: "+level + " Resources: "+xp;
    }
}
