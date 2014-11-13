package com.bt.frontier;

import android.os.AsyncTask;

import com.goebl.david.Response;
import com.goebl.david.Webb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by curtiskennington on 12/11/14.
 */
public class NodeCaptureTask extends AsyncTask<Object[], Void, String> {

    private MainActivity mainActivity;
    private Node node;

    @Override
    protected String doInBackground(Object[]... params) {

        this.node = (Node)params[0][0];
        mainActivity = (MainActivity)params[0][1];
        Webb webb = Webb.create();
        String requestSuccess = "failure";

        try{
            JSONObject request = new JSONObject();
            request.put("owner", ((FrontierApp)mainActivity.getApplication()).getUserId());

            JSONArray locals = new JSONArray();
            for(User user : mainActivity.getUsersInRange()){
                locals.put(user.getOpenId());
            }

            request.put("locals", locals);

            Response<JSONObject> response = webb
                    .put("http://192.168.43.153:4567/nodes/"+node.getId())
                    .body(request)
                    .ensureSuccess()
                    .asJsonObject();

            JSONObject apiResult = response.getBody();
            requestSuccess = apiResult.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return requestSuccess;
    }

    @Override
    protected void onPostExecute(String result){
        boolean isSuccess = false;
        if(result.equals("success")){
            isSuccess = true;
        }

        mainActivity.captureNodeCallback(isSuccess, node);
    }
}
