package com.bt.frontier;

import android.os.AsyncTask;

import com.goebl.david.Webb;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by curtiskennington on 12/11/14.
 */
public class NodeFetchTask extends AsyncTask<MainActivity, Void, ArrayList<Node>> {

    MainActivity mainActivity;

    @Override
    protected ArrayList<Node> doInBackground(MainActivity... params) {
        mainActivity = params[0];

        ArrayList<Node> nodes = new ArrayList<Node>();

        Webb webb = Webb.create();
        JSONArray result = webb.get("http://192.168.43.153:4567/nodes")
                .ensureSuccess()
                .asJsonArray()
                .getBody();

        for(int i = 0; i < result.length(); i++){
            try {
                JSONObject object = (JSONObject) result.get(i);
                nodes.add(new Node(object));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return nodes;
    }

    protected void onPostExecute(ArrayList<Node> nodes){
        mainActivity.setNodes(nodes);
    }
}
