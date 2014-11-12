package com.bt.frontier;

import android.app.Application;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by curtiskennington on 12/11/14.
 */
public class FrontierApp extends Application {

    private Location currentLocation;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String user;

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
