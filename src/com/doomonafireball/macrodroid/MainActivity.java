package com.doomonafireball.macrodroid;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
    private Button todayBTN;
    private Button previousBTN;
    private Button myFoodsBTN;
    private Button myMacrosBTN;
    private Button settingsBTN;
    private Context mContext;
    private MacroDroidApplication application;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mContext = this;
        application = ((MacroDroidApplication) getApplication());

        todayBTN = (Button) findViewById(R.id.BTN_today);
        previousBTN = (Button) findViewById(R.id.BTN_previous);
        myFoodsBTN = (Button) findViewById(R.id.BTN_my_foods);
        myMacrosBTN = (Button) findViewById(R.id.BTN_my_macros);
        settingsBTN = (Button) findViewById(R.id.BTN_settings);

        todayBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (application.hasData(mContext)) {
                    // Try to find the ADay object
                    List<ADay> possibleCurrDays =
                        application.getADayForCalendar(Calendar.getInstance());
                    if (!possibleCurrDays.isEmpty()) {
                        // We have a current object
                        Log.d(Tags.LOG_TAG, "We have a current day for: "
                                + Calendar.getInstance().get(Calendar.MONTH) + "/"
                                + Calendar.getInstance().get(Calendar.DATE) + "/"
                                + Calendar.getInstance().get(Calendar.YEAR));
                    } else {
                        // Instantiate a new object
                        Log.d(Tags.LOG_TAG, "We don't have a current day for: "
                                + Calendar.getInstance().get(Calendar.MONTH) + "/"
                                + Calendar.getInstance().get(Calendar.DATE) + "/"
                                + Calendar.getInstance().get(Calendar.YEAR));
                        ADay currDay = new ADay();
                        currDay.setDate(Calendar.getInstance());
                        application.saveDay(currDay);
                    }
                } else {
                    // Instantiate a new object
                    Log.d(Tags.LOG_TAG,
                            "We don't even have data! AND We don't have a current day for: "
                                    + Calendar.getInstance().get(Calendar.MONTH) + "/"
                                    + Calendar.getInstance().get(Calendar.DATE) + "/"
                                    + Calendar.getInstance().get(Calendar.YEAR));
                    ADay currDay = new ADay();
                    currDay.setDate(Calendar.getInstance());
                    application.saveDay(currDay);
                }
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
                startActivity(new Intent(MainActivity.this, MyFoodsActivity.class));
            }
        });
        myMacrosBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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