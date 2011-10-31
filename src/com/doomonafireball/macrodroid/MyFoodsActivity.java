package com.doomonafireball.macrodroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MyFoodsActivity extends Activity {
    private Button addBTN;
    private Button importBTN;
    private ListView myFoodsLV;
    private MacroDroidApplication application;
    private Context mContext;
    private byte[] myFoodsByteArray;
    private String myFoods;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_foods_activity);

        application = ((MacroDroidApplication) getApplication());
        mContext = this;

        addBTN = (Button) findViewById(R.id.BTN_add);
        importBTN = (Button) findViewById(R.id.BTN_import);
        myFoodsLV = (ListView) findViewById(R.id.LV_my_foods);

        addBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                
            }
        });
        importBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                
            }
        });
        
        myFoodsByteArray = application.readInternalStoragePrivate(Tags.MY_FOODS_FILENAME);
        myFoods = new String(myFoodsByteArray);
        Log.d(Tags.LOG_TAG, myFoods);
    }
}
