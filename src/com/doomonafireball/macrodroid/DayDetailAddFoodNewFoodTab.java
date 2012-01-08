package com.doomonafireball.macrodroid;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class DayDetailAddFoodNewFoodTab extends Activity {
	private MacroDroidApplication application;
	private Context mContext;
	private EditText nameET;
	private EditText servingSizeET;
	private EditText numServingsET;
	private EditText kCalET;
	private EditText proteinET;
	private EditText carbsET;
	private EditText fatET;
	private Button addBTN;
	private Button cancelBTN;
	private Calendar mCalendar;
	private ADay mADay;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.day_detail_add_food_new_food_tab);

		application = ((MacroDroidApplication) getApplication());
		mContext = this;

		// Parse the extras
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int mDate = extras.getInt("date");
			int mMonth = extras.getInt("month");
			int mYear = extras.getInt("year");
			mCalendar = Calendar.getInstance();
			mCalendar.set(Calendar.DATE, mDate);
			mCalendar.set(Calendar.MONTH, mMonth);
			mCalendar.set(Calendar.YEAR, mYear);
		} else {
			// TODO Something went wrong (this should never happen)
		}

		mADay = application.safelyFetchExistingADay(mCalendar);

		nameET = (EditText) findViewById(R.id.ET_add_food_name);
		servingSizeET = (EditText) findViewById(R.id.ET_add_food_serving_size);
		numServingsET = (EditText) findViewById(R.id.ET_add_food_num_servings);
		kCalET = (EditText) findViewById(R.id.ET_add_food_kCal);
		proteinET = (EditText) findViewById(R.id.ET_add_food_protein);
		carbsET = (EditText) findViewById(R.id.ET_add_food_carbs);
		fatET = (EditText) findViewById(R.id.ET_add_food_fat);
		addBTN = (Button) findViewById(R.id.BTN_add);
		cancelBTN = (Button) findViewById(R.id.BTN_cancel);

		addBTN.setOnClickListener(addOnClickListener);
		cancelBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private OnClickListener addOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// Perist to saved foods
			AFood food = new AFood();
			food.setFoodName(nameET.getText().toString());
			food.setFoodServingSize(servingSizeET.getText().toString());
			food.setFoodKCal(Float.parseFloat(kCalET.getText().toString()));
			food.setFoodGramsProtein(Float.parseFloat(proteinET.getText()
					.toString()));
			food.setFoodGramsCarbs(Float.parseFloat(carbsET.getText()
					.toString()));
			food.setFoodGramsFat(Float.parseFloat(fatET.getText().toString()));

			application.addFoodToInternalStorage(food);

			// Persist to ADay object
			Float servingsValue = Float.parseFloat(numServingsET.getText()
					.toString());
			ArrayList<Pair<Float, AFood>> mFoods = mADay.getFoods();
			if (mFoods == null) {
				mFoods = new ArrayList<Pair<Float, AFood>>();
			}
			mFoods.add(new Pair<Float, AFood>(servingsValue, food));
			mADay.setFoods(mFoods);
			application.saveDay(mADay);	
			
			finish();
		}
	};
}
