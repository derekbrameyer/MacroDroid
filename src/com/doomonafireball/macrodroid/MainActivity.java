package com.doomonafireball.macrodroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
    private Button todayBTN;
    private Button previousBTN;
    private Button myFoodsBTN;
    private Button myMacrosBTN;
    private Button settingsBTN;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        todayBTN = (Button) findViewById(R.id.BTN_today);
        previousBTN = (Button) findViewById(R.id.BTN_previous);
        myFoodsBTN = (Button) findViewById(R.id.BTN_my_foods);
        myMacrosBTN = (Button) findViewById(R.id.BTN_my_macros);
        settingsBTN = (Button) findViewById(R.id.BTN_settings);

        todayBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                }
        });
        previousBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                }
        });
        myFoodsBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                }
        });
        myMacrosBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(MainActivity.this, MyMacrosActivity.class));
            }
        });
        settingsBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                }
        });
    }
}