package com.bt.frontier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bt.R;
//import com.razer.android.nabuopensdk.NabuOpenSDK;
//import com.razer.android.nabuopensdk.interfaces.NabuAuthListener;
import com.goebl.david.Webb;

public class MainActivity extends Activity {

    //NabuOpenSDK nabuSdk;
    private static final String NABU_CLIENT_ID = "79f02472157d21c19315983c78ba574be9df09dd";
    private static final String GOOGLE_MAPS_API_KEY = "";
    private static String[] testScope = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testScope[0] = "TEST";

//        nabuSdk.initiate(this, NABU_CLIENT_ID, testScope, new NabuAuthListener() {
//
//            @Override
//            public void onAuthSuccess(String s) {
//
//            }
//
//            @Override
//            public void onAuthFailed(String s) {
//
//            }
//        });
        Log.d("before calling get nodes","");
        getNodes();
    }
    
    private void getNodes() {
		Webb webb = Webb.create();
		JSONArray result = webb.get("http://192.168.43.153:4567/nodes")
				.ensureSuccess()
				.asJsonArray()
				.getBody();
	
		for(int i = 0; i<result.length(); i++){
			try {
				JSONObject object = (JSONObject) result.get(i);
				Log.d("http response:", object.getString("name"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
    }

//    public void getNodes(){
//    	HttpClient httpClient = new DefaultHttpClient();
//    	HttpGet get = new HttpGet("http://192.168.43.153:4567/nodes");
//    	try {
//			HttpResponse response = httpClient.execute(get);
//			
//			Log.d("http response:", getResponseString(response));
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }

    public String getResponseString(HttpResponse response){
    	BufferedReader br = null;
		StringBuilder result = new StringBuilder();

    	try {
			br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	    	String line;
	    	
	    	while((line = br.readLine()) != null){
	    		result.append(line);
	    	}
	    	
	    	
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	return result.toString();
    	
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
