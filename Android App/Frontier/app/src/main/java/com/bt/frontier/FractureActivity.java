package com.bt.frontier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.bt.R;

/**
 * Created by will on 13/11/2014.
 */
public class FractureActivity extends Activity {

    private Button captureButton;

    private Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fracture_view);

        captureButton = (Button) findViewById(R.id.captureButton);

        captureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Set the data to pass back
                setResult(RESULT_OK);
                finish();

            }
        });

        closeButton = (Button) findViewById(R.id.closeButton);
        captureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent data = new Intent();

                //Set the data to pass back
               // data.setData("captured");
                setResult(RESULT_CANCELED);

                finish();

            }
        });


    }

}

