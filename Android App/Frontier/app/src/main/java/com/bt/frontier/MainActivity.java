package com.bt.frontier;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.content.Intent;
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
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.razer.android.nabuopensdk.NabuOpenSDK;
import com.razer.android.nabuopensdk.interfaces.NabuAuthListener;
import com.razer.android.nabuopensdk.interfaces.SendNotificationListener;
import com.razer.android.nabuopensdk.models.NabuNotification;
import com.razer.android.nabuopensdk.models.Scope;

import java.util.Timer;
import java.util.HashMap;

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
    private ArrayList<User> usersInRange;
    private String[] userIdsInRange;
    private static HashMap<Marker, Node> mapMarkerNodes;

    private Marker heldMarkerFromView;

    final int request_code = 1;


    private FrontierApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = ((FrontierApp)getApplicationContext());
        app.setUserId("ab990d3a9c8100889a2fb5b04567ec1f0ba086ce5e58da4abb513c12b30ed6ef");

        this.setUserIdsInRange(new String[]{});
        this.setUsersInRange(new ArrayList<User>());

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
            mapMarkerNodes = new HashMap<Marker, Node>();
            new NodeFetchTask().execute(this);
            new UserFetchTask().execute(this);

            initializeMap();
        } catch (Exception e){
            e.printStackTrace();
        }

        googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MainActivity.this,FractureActivity.class);
                heldMarkerFromView = marker;
                startActivityForResult(intent, request_code);
            }

        });


    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == request_code){
            if(resultCode == RESULT_OK){
                if (data.getData().toString().equals("cap")){
                    Node node = mapMarkerNodes.get(heldMarkerFromView);
                    new NodeCaptureTask().execute(new Object[]{node, this});

                } else {
                    Toast.makeText(this, "Run From Fracture " ,Toast.LENGTH_SHORT).show();

                }
                heldMarkerFromView = null;
            }
        }
    }

    public void captureNodeCallback(boolean isSuccess, Node node){
        if(isSuccess){
            Toast.makeText(this, "Successfully captured ", Toast.LENGTH_LONG).show();
            nabuSdk.sendNotification(this, new NabuNotification("frontier", "Success", node.getName()), new SendNotificationListener() {

                @Override
                public void onSuccess(String s) {
                    Log.d("notification", "SUCCESS");
                }

                @Override
                public void onFailed(String s) {
                    Log.d("notification", "FAILED");
                }
            });

            new NodeFetchTask().execute(this);
            new UserFetchTask().execute(this);
        } else {
            Toast.makeText(this, "Unable to capture - insufficient resources!", Toast.LENGTH_LONG).show();
            nabuSdk.sendNotification(this, new NabuNotification("frontier", "Insufficient Resources!", node.getName()), new SendNotificationListener() {
                @Override
                public void onSuccess(String s) {
                    Log.d("notification", "SUCCESS");
                }

                @Override
                public void onFailed(String s) {
                    Log.d("notification", "FAILED");
                }
            });
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

    private void addMapMarker(Node node){
        //TODO: Check googleMap exists
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(node.getLat(), node.getLon()))
                .title(node.toString())
                .icon(BitmapDescriptorFactory.defaultMarker(this.getMarkerColour(node))));

        mapMarkerNodes.put(marker, node);
    }

    private void removeAllMapMarkers(){
        for(Marker marker : mapMarkerNodes.keySet()){
            marker.remove();
        }
        mapMarkerNodes.clear();
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
            addMapMarker(node);
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

    public void setUsersInRange(ArrayList<User> users) {
        this.usersInRange = users;
        TextView players = (TextView)this.findViewById(R.id.localPlayersLabel);

        StringBuilder playerString = new StringBuilder("Local Players ("+users.size()+"): \n");
        for(User player : users){
            playerString.append(player.toString() + "\n");
        }

        players.setText(playerString);
    }

    public void addNodeCallback(){
        Toast.makeText(this, "Add success", Toast.LENGTH_SHORT).show();
        new NodeFetchTask().execute(this);
        new UserFetchTask().execute(this);
    }

    public ArrayList<User> getUsersInRange() {
        return this.usersInRange;
    }

    public void setUserIdsInRange(String[] ids){
        this.userIdsInRange = ids;
        new MultipleUserFetchTask().execute(this);
    }

    public String[] getUserIdsInRange(){
        return this.userIdsInRange;
    }

    private float getMarkerColour(Node node){
        if(node.getFaction() == 1){
            return BitmapDescriptorFactory.HUE_GREEN;
        }

        return BitmapDescriptorFactory.HUE_RED;

    }
}
