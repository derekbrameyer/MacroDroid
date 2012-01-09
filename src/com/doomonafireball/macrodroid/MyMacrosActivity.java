package com.doomonafireball.macrodroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

public class MyMacrosActivity extends FragmentActivity {
    private Button calculateBTN;
    private Button iKnowBTN;
    private TextView noInputTV;
    private TextView trainingKCalTV;
    private TextView restKCalTV;
    private TextView deficitTV;
    private TextView proteinTV;
    private TextView carbsTV;
    private TextView fatTV;
    private TableLayout macrosTL;
    private MacroDroidApplication application;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_macros_activity);

        application = ((MacroDroidApplication) getApplication());
        
        final ActionBar ab = getSupportActionBar();
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle(getResources().getString(R.string.My_Macros));

        calculateBTN = (Button) findViewById(R.id.BTN_calculate);
        iKnowBTN = (Button) findViewById(R.id.BTN_i_know);
        noInputTV = (TextView) findViewById(R.id.TV_no_input);
        trainingKCalTV = (TextView) findViewById(R.id.TV_training_kCal);
        restKCalTV = (TextView) findViewById(R.id.TV_rest_kCal);
        deficitTV = (TextView) findViewById(R.id.TV_deficit);
        proteinTV = (TextView) findViewById(R.id.TV_protein);
        carbsTV = (TextView) findViewById(R.id.TV_carbs);
        fatTV = (TextView) findViewById(R.id.TV_fat);
        macrosTL = (TableLayout) findViewById(R.id.TL_macros);
        
        showData();

        calculateBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Calculate Activity
                startActivity(new Intent(MyMacrosActivity.this,
                        MyMacrosCalculateActivity.class));
            }
        });
        iKnowBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch IKnow Activity
                startActivity(new Intent(MyMacrosActivity.this,
                        MyMacrosIKnowActivity.class));

            }
        });
    }
    
    @Override
    public void onResume() {
        super.onResume();
        showData();
    }
    
    private void showData() {
        // Hide/show noInputTV vs. macrosTL
        if (application.macrosPrefs.contains(Tags.MACROS_TRAINING_KCAL)) {
            // We have macros!
            noInputTV.setVisibility(View.GONE);
            macrosTL.setVisibility(View.VISIBLE);
            trainingKCalTV.setText(Float.toString(application.macrosPrefs.getFloat(
                    Tags.MACROS_TRAINING_KCAL, 0.0f)));
            restKCalTV.setText(Float.toString(application.macrosPrefs.getFloat(
                    Tags.MACROS_REST_KCAL, 0.0f)));
            deficitTV.setText(Float.toString(application.macrosPrefs.getFloat(
                    Tags.MACROS_DEFICIT, 0.0f)));
            proteinTV.setText(Float.toString(application.macrosPrefs.getFloat(
                    Tags.MACROS_PROTEIN, 0.0f)));
            carbsTV.setText(Float.toString(application.macrosPrefs
                    .getFloat(Tags.MACROS_CARBS, 0.0f)));
            fatTV.setText(Float.toString(application.macrosPrefs.getFloat(Tags.MACROS_FAT, 0.0f)));
        } else {
            // We don't have macros!
            noInputTV.setVisibility(View.VISIBLE);
            macrosTL.setVisibility(View.GONE);
        }        
    }
}
