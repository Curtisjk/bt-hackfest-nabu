package com.bt.frontier;

import android.app.Application;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by curtiskennington on 12/11/14.
 */
public class FrontierApp extends Application {

    private Location currentLocation;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String user) {
        this.userId = user;
    }

    private String userId;

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
