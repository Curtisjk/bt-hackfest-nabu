package com.bt.frontier.frontier;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.razer.android.nabuopensdk.NabuOpenSDK;
import com.razer.android.nabuopensdk.interfaces.NabuAuthListener;


public class MainActivity extends Activity {

    NabuOpenSDK nabuSdk;
    private static final String NABU_CLIENT_ID = "79f02472157d21c19315983c78ba574be9df09dd";
    private static final String GOOGLE_MAPS_API_KEY = "";
    private static String[] testScope = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testScope[0] = "TEST";

        nabuSdk.initiate(this, NABU_CLIENT_ID, testScope, new NabuAuthListener() {

            @Override
            public void onAuthSuccess(String s) {

            }

            @Override
            public void onAuthFailed(String s) {

            }
        });
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
}
