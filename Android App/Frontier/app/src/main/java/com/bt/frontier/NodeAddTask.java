package com.bt.frontier;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.goebl.david.Response;
import com.goebl.david.Webb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by curtiskennington on 12/11/14.
 */
public class NodeAddTask extends AsyncTask<Object[], Void, Void> {

    private MainActivity mainActivity;

    @Override
    protected Void doInBackground(Object[]... params) {

        Node node = (Node)params[0][0];
        mainActivity = (MainActivity)params[0][1];
        Webb webb = Webb.create();

        try{
            JSONObject request = new JSONObject();
            request.put("owner", node.getOwner());
            request.put("name", node.getName());
            request.put("lat", node.getLat());
            request.put("long", node.getLon());

            Response<JSONObject> response = webb
                    .post("http://192.168.43.153:4567/nodes")
                    .body(request)
                    .ensureSuccess()
                    .asJsonObject();

            JSONObject apiResult = response.getBody();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void v){
        mainActivity.addNodeCallback();
    }
}
