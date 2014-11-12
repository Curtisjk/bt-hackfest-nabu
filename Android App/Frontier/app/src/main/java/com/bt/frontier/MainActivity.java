package com.bt.frontier;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bt.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.razer.android.nabuopensdk.NabuOpenSDK;
import com.razer.android.nabuopensdk.interfaces.NabuAuthListener;
import com.razer.android.nabuopensdk.models.Scope;

import java.util.Timer;

public class MainActivity extends FragmentActivity implements
    GooglePlayServicesClient.ConnectionCallbacks,
    GooglePlayServicesClient.OnConnectionFailedListener {

    private static final String NABU_CLIENT_ID = "79f02472157d21c19315983c78ba574be9df09dd";
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private static String[] testScope;
    private int peopleInRange;
    private static NabuOpenSDK nabuSdk = null;

    private LocationClient locationClient;
    private ArrayList<Node> nodes = new ArrayList<Node>();
    private LocationClient mLocationClient;
    private GoogleMap googleMap;
    private ImageButton addNodeButton;
    private User user;
    private String[] idsInRange;
    private ArrayList<Marker> mapsMarkers;


    private FrontierApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = ((FrontierApp)getApplicationContext());
        app.setUserId("fb990d3a9c8100889a2fb5b04567ec1f0ba086ce5e58da4abb513c12b30ed6ea");

        this.setIdsInRange(new String[]{});

        addNodeButton = (ImageButton) findViewById(R.id.addNodeButton);

        addNodeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addNodeButtonPress();
            }
        });

        locationClient = new LocationClient(this, this, this);

        testScope = new String[] {Scope.SCOPE_FITNESS};

        nabuSdk = NabuOpenSDK.getInstance(this);
        nabuSdk.initiate(this, NABU_CLIENT_ID, testScope, new NabuAuthListener() {

            @Override
            public void onAuthSuccess(String s) {


                Timer t = new Timer();
                t.schedule(new PulseUpdateTask(MainActivity.this, nabuSdk),500, 10000);

            }

            @Override
            public void onAuthFailed(String s) {

            }
        });

        try{
            new NodeFetchTask().execute(this);
            new UserFetchTask().execute(this);

            mapsMarkers = new ArrayList<Marker>();
            initializeMap();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeMap() {

        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            googleMap.setMyLocationEnabled(true);
        }
    }

    private void addMapMarker(double lat, double lon, String title){
        //TODO: Check googleMap exists
        mapsMarkers.add(googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title(title)));
    }

    private void removeAllMapMarkers(){
        for(Marker marker : mapsMarkers){
            marker.remove();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationClient.connect();
    }

    @Override
    protected void onStop(){
        locationClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume(){
        super.onResume();
        this.initializeMap();
    }

    @Override
    protected void onDestroy() {
        nabuSdk.onDestroy(this);
        super.onDestroy();
    }

    public int getPeopleInRange() {
        return peopleInRange;
    }

    public void setPeopleInRange(int peopleInRange) {
        this.peopleInRange = peopleInRange;
    }

    public void setNodes(ArrayList<Node> nodes){
        this.removeAllMapMarkers();
        this.nodes = nodes;

        for(Node node : nodes){
            addMapMarker(node.getLat(), node.getLon(), node.toString());
        }
    }


    @Override
    public void onConnected(Bundle dataBundle) {
        app.setCurrentLocation(locationClient.getLastLocation());

        Location currentLocation = locationClient.getLastLocation();

        LatLng coordinate = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinate, 13);
        googleMap.animateCamera(location);
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("onConnectionFailed", "onConnectionFailed: " + connectionResult.getErrorCode());
        }
    }

    private void addNodeButtonPress(){
        DialogFragment newFragment = new AddNodeDialog();
        newFragment.show(this.getFragmentManager(), "addNode");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {

        TextView userText = (TextView) this.findViewById(R.id.textView);
        userText.setText(user.toString());
        this.user = user;
    }

    public void setIdsInRange(String[] idsInRange) {
        this.idsInRange = idsInRange;
    }

    public void addNodeCallback(){
        Toast.makeText(this, "Add success", Toast.LENGTH_SHORT).show();
        new NodeFetchTask().execute(this);
        new UserFetchTask().execute(this);
    }

    public String[] getIdsInRange() {
        return idsInRange;
    }
}
