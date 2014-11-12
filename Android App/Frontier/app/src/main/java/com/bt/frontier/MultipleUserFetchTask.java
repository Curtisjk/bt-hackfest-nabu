package com.bt.frontier;

import android.os.AsyncTask;

import com.goebl.david.Response;
import com.goebl.david.Webb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by curtiskennington on 12/11/14.
 */
public class MultipleUserFetchTask extends AsyncTask<MainActivity, Void, ArrayList<User>> {

    private MainActivity mainActivity;
    private String[] userIds;

    @Override
    protected ArrayList<User> doInBackground(MainActivity... mainActivities) {

        mainActivity = mainActivities[0];
        userIds = mainActivity.getUserIdsInRange();
        FrontierApp app = (FrontierApp)(mainActivity.getApplicationContext());

        ArrayList<User> users = new ArrayList<User>();

        try {
            JSONArray userIdArray = new JSONArray();

            for(String id : userIds){
                userIdArray.put(id);
            }

            Webb webb = Webb.create();
            JSONObject request = new JSONObject();
            request.put("users", userIdArray);


            JSONArray result = webb.post("http://192.168.43.153:4567/userlist")
                    .body(request)
                    .ensureSuccess()
                    .asJsonArray()
                    .getBody();

            for(int i = 0; i < result.length(); i++){
                JSONObject object = (JSONObject) result.get(i);
                users.add(new User(object));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return users;

    }

    protected void onPostExecute(ArrayList<User> users){
        mainActivity.setUsersInRange(users);
    }

}
