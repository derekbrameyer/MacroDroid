package com.doomonafireball.macrodroid;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MyFoodsActivity extends Activity {
    private Button addBTN;
    private Button importBTN;
    private ListView myFoodsLV;
    private MyFoodsAdapter myFoodsAdapter;
    private MacroDroidApplication application;
    private Context mContext;
    private ArrayList<AFood> myFoodsArrayList;

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
                startActivity(new Intent(MyFoodsActivity.this, MyFoodsAddActivity.class));
            }
        });
        importBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                application.writeInternalStoragePrivate(Tags.MY_FOODS_FILENAME,
                        application.defaultFoodsToByteArray());
                refreshList();
            }
        });

        refreshList();

        myFoodsLV.setOnItemLongClickListener(myFoodsItemLongClickListener);
    }

    private OnItemLongClickListener myFoodsItemLongClickListener = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
            // TODO Auto-generated method stub
            final AFood selectedFood = (AFood) myFoodsAdapter.getItem(position);

            final CharSequence[] items = { "Delete" };
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Options");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    if (item == 0) {
                        // Delete this food
                        myFoodsArrayList.remove(selectedFood);
                        application.writeFoodsToInternalStorage(myFoodsArrayList);
                        refreshList();
                        dialog.dismiss();
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
    };

    private void refreshList() {
    	myFoodsArrayList = application.getAllFoods();
        myFoodsAdapter = new MyFoodsAdapter(mContext, myFoodsArrayList);
        myFoodsLV.setAdapter(myFoodsAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }
}
