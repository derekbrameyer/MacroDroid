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
    private byte[] myFoodsByteArray;
    private String myFoodsRaw;
    private String[] myFoods;
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
        myFoodsByteArray = application.readInternalStoragePrivate(Tags.MY_FOODS_FILENAME);
        myFoodsRaw = new String(myFoodsByteArray);
        myFoods = myFoodsRaw.split("\\r?\\n");

        // Parse the String array into an ArrayList<AFood>
        myFoodsArrayList = new ArrayList<AFood>();
        if (myFoods != null) {
            if (myFoods.length > 5) {
                for (int i = 0; i < myFoods.length; i += 7) {
                    AFood food = new AFood();

                    Log.d(Tags.LOG_TAG, "Name: " + myFoods[i]);
                    Log.d(Tags.LOG_TAG, "Serving size: " + myFoods[i + 1]);
                    Log.d(Tags.LOG_TAG, "kCal: " + myFoods[i + 2]);
                    Log.d(Tags.LOG_TAG, "Protein: " + myFoods[i + 3]);
                    Log.d(Tags.LOG_TAG, "Carbs: " + myFoods[i + 4]);
                    Log.d(Tags.LOG_TAG, "Fat: " + myFoods[i + 5]);

                    food.setFoodName(myFoods[i]);
                    food.setFoodServingSize(myFoods[i + 1]);
                    food.setFoodKCal(Float.parseFloat(myFoods[i + 2]));
                    food.setFoodGramsProtein(Float.parseFloat(myFoods[i + 3]));
                    food.setFoodGramsCarbs(Float.parseFloat(myFoods[i + 4]));
                    food.setFoodGramsFat(Float.parseFloat(myFoods[i + 5]));

                    myFoodsArrayList.add(food);
                }                
            }
        }

        myFoodsAdapter = new MyFoodsAdapter(mContext, myFoodsArrayList);
        myFoodsLV.setAdapter(myFoodsAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }
}
