package com.doomonafireball.macrodroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyFoodsAddActivity extends Activity {
    private MacroDroidApplication application;
    private Button saveBTN;
    private Button cancelBTN;
    private EditText nameET;
    private EditText servingSizeET;
    private EditText kCalET;
    private EditText proteinET;
    private EditText carbsET;
    private EditText fatET;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_foods_add_activity);
        
        application = ((MacroDroidApplication) getApplication());
        
        saveBTN = (Button) findViewById(R.id.BTN_save);
        cancelBTN = (Button) findViewById(R.id.BTN_cancel);
        nameET = (EditText) findViewById(R.id.ET_add_food_name);
        servingSizeET = (EditText) findViewById(R.id.ET_add_food_serving_size);
        kCalET = (EditText) findViewById(R.id.ET_add_food_kCal);
        proteinET = (EditText) findViewById(R.id.ET_add_food_protein);
        carbsET = (EditText) findViewById(R.id.ET_add_food_carbs);
        fatET = (EditText) findViewById(R.id.ET_add_food_fat);
        
        saveBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Persist values to storage
                AFood food = new AFood();
                food.setFoodName(nameET.getText().toString());
                food.setFoodServingSize(servingSizeET.getText().toString());
                food.setFoodKCal(Float.parseFloat(kCalET.getText().toString()));
                food.setFoodGramsProtein(Float.parseFloat(proteinET.getText().toString()));
                food.setFoodGramsCarbs(Float.parseFloat(carbsET.getText().toString()));
                food.setFoodGramsFat(Float.parseFloat(fatET.getText().toString()));
                
                application.addFoodToInternalStorage(food);
                finish();
            }            
        });
        cancelBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }            
        });        
    }
}
