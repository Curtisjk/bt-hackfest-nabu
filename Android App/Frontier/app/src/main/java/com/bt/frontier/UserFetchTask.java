package com.bt.frontier;

import android.os.AsyncTask;

import com.goebl.david.Webb;

import org.json.JSONObject;

/**
 * Created by jonathan on 12/11/14.
 */
public class UserFetchTask extends AsyncTask<MainActivity, Void, User> {

    private MainActivity mainActivity;

    @Override
    protected User doInBackground(MainActivity... mainActivities) {

        mainActivity = mainActivities[0];
        FrontierApp app = (FrontierApp)(mainActivity.getApplicationContext());

        Webb webb = Webb.create();
        JSONObject result = webb.get("http://192.168.43.153:4567/users/"+app.getUserId())
                .ensureSuccess()
                .asJsonObject()
                .getBody();

        User user = new User(result);

        return user;

    }

    protected void onPostExecute(User user){
        mainActivity.setUser(user);
    }

}
