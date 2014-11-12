package com.bt.frontier;

import android.util.Log;
import android.widget.Toast;

import com.razer.android.nabuopensdk.NabuOpenSDK;
import com.razer.android.nabuopensdk.interfaces.PulseListener;
import com.razer.android.nabuopensdk.interfaces.ReadClipboardListener;
import com.razer.android.nabuopensdk.interfaces.UserProfileListener;
import com.razer.android.nabuopensdk.models.ClipboardData;
import com.razer.android.nabuopensdk.models.PulseData;
import com.razer.android.nabuopensdk.models.UserProfile;

import java.util.Calendar;
import java.util.TimerTask;

/**
 * Created by jonathan on 12/11/14.
 */
public class UpdateTask extends TimerTask {


    private final MainActivity mainActivity;
    private NabuOpenSDK nabuSdk = null;

    public UpdateTask(MainActivity mainActivity, NabuOpenSDK sdk){
        nabuSdk = sdk;
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -1);


        nabuSdk.getPulseData(mainActivity, c.getTimeInMillis(), System.currentTimeMillis(), new PulseListener() {
            @Override
            public void onReceiveData(PulseData[] pulseDatas) {
//                Toast.makeText(mainActivity,
//                        "number of pulses " + pulseDatas[pulseDatas.length - 1].timeStamp + " " + pulseDatas[pulseDatas.length - 1].openIds.length , Toast.LENGTH_SHORT)
//                        .show();
                mainActivity.setPeopleInRange(pulseDatas[pulseDatas.length - 1].openIds.length);
            }

            @Override
            public void onReceiveFailed(String s) {

            }
        });


    }
}
