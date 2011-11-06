package com.doomonafireball.macrodroid;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class DayDetailAddFoodMyFoodsTab extends Activity {
	private MacroDroidApplication application;
	private Context mContext;
	private ListView myFoodsLV;
	private Calendar mCalendar;
	private MyFoodsAdapter myFoodsAdapter;
	private ArrayList<AFood> myFoodsArrayList;
	private ADay mADay;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.day_detail_add_food_my_foods_tab);

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

		myFoodsLV = (ListView) findViewById(R.id.LV_my_foods);

		fetchExistingADay();
		refreshList();

		myFoodsLV.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				final AFood selectedFood = (AFood) myFoodsAdapter
						.getItem(position);

				AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
				alert.setTitle("Servings");
				// Set an EditText view to get user input
				final EditText input = new EditText(mContext);
				input.setText("1.0");
				alert.setView(input);
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Float servingsValue = Float.parseFloat(input
										.getText().toString());
								// Do something with value!
								ArrayList<AFood> mFoods = mADay.getFoods();
								ArrayList<Float> mServings = mADay
										.getServings();
								if (mFoods == null) {
									mFoods = new ArrayList<AFood>();
								}
								if (mServings == null) {
									mServings = new ArrayList<Float>();
								}
								mFoods.add(selectedFood);
								mServings.add(servingsValue);
								mADay.setFoods(mFoods);
								mADay.setServings(mServings);
								application.saveDay(mADay);
								// Just changing stuff to re-run
								finish();
							}
						});
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						});
				alert.show();
			}
		});
	}

	private void fetchExistingADay() {
		if (application.hasData(mContext)) {
			// Try to find the ADay object
			List<ADay> possibleCurrDays = application
					.getADayForCalendar(mCalendar);
			if (!possibleCurrDays.isEmpty()) {
				// We have a current object
				Log.d(Tags.LOG_TAG, "We have a current day for today.");
				mADay = possibleCurrDays.get(0);
			} else {
				// Instantiate a new object
				Log.d(Tags.LOG_TAG, "We don't have a current day for today.");
				mADay = new ADay(mCalendar, true);
				application.saveDay(mADay);
			}
		} else {
			// Instantiate a new object
			Log.d(Tags.LOG_TAG,
					"We don't even have data! AND We don't have a current day.");
			mADay = new ADay(mCalendar, true);
			application.saveDay(mADay);
		}
	}

	private void refreshList() {
		myFoodsArrayList = application.getAllFoods();
		myFoodsAdapter = new MyFoodsAdapter(mContext, myFoodsArrayList);
		myFoodsLV.setAdapter(myFoodsAdapter);
	}
}
