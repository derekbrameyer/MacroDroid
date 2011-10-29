package com.doomonafireball.macrodroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

public class MyMacrosActivity extends Activity {
    private Button calculateBTN;
    private Button iKnowBTN;
    private TextView noInputTV;
    private TableLayout macrosTL;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_macros_activity);

        calculateBTN = (Button) findViewById(R.id.BTN_calculate);
        iKnowBTN = (Button) findViewById(R.id.BTN_i_know);
        noInputTV = (TextView) findViewById(R.id.TV_no_input);
        macrosTL = (TableLayout) findViewById(R.id.TL_macros);

        // TODO Hide/show noInputTV vs. macrosTL

        calculateBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Calculate Activity
                startActivity(new Intent(MyMacrosActivity.this, MyMacrosCalculateActivity.class));
            }
        });
        iKnowBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                }
        });
    }
}
