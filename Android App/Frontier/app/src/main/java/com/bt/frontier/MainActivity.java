package com.bt.frontier;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bt.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.razer.android.nabuopensdk.NabuOpenSDK;
import com.razer.android.nabuopensdk.interfaces.Hi5Listener;
import com.razer.android.nabuopensdk.interfaces.NabuAuthListener;
import com.razer.android.nabuopensdk.interfaces.PulseListener;
import com.razer.android.nabuopensdk.interfaces.ReadClipboardListener;
import com.razer.android.nabuopensdk.interfaces.UserProfileListener;
import com.razer.android.nabuopensdk.models.ClipboardData;
import com.razer.android.nabuopensdk.models.Hi5Data;
import com.razer.android.nabuopensdk.models.PulseData;
import com.razer.android.nabuopensdk.models.Scope;
import com.razer.android.nabuopensdk.models.UserProfile;

import java.util.Calendar;
import java.util.Timer;

public class MainActivity extends Activity {

    static NabuOpenSDK nabuSdk = null;
    private static final String NABU_CLIENT_ID = "79f02472157d21c19315983c78ba574be9df09dd";
    private static final String GOOGLE_MAPS_API_KEY = "";
    private static String[] testScope;
    private int peopleInRange;

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testScope = new String[] {Scope.SCOPE_FITNESS};

        nabuSdk = NabuOpenSDK.getInstance(this);
        nabuSdk.initiate(this, NABU_CLIENT_ID, testScope, new NabuAuthListener() {

            @Override
            public void onAuthSuccess(String s) {


                Timer t = new Timer();
                t.schedule(new UpdateTask(MainActivity.this, nabuSdk),500, 10000);

            }

            @Override
            public void onAuthFailed(String s) {

            }
        });

        try{
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
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
}
