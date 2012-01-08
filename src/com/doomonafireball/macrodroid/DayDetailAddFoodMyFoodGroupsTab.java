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

public class DayDetailAddFoodMyFoodGroupsTab extends Activity {
	private MacroDroidApplication application;
	private Context mContext;
	private ListView myFoodGroupsLV;
	private Calendar mCalendar;
	private MyFoodGroupsAdapter myFoodGroupsAdapter;
	private ArrayList<AFoodGroup> myFoodGroupsArrayList;
	private ADay mADay;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.day_detail_add_food_my_food_groups_tab);

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

		myFoodGroupsLV = (ListView) findViewById(R.id.LV_my_food_groups);

		mADay = application.safelyFetchExistingADay(mCalendar);
		refreshList();

		myFoodGroupsLV.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				final AFoodGroup selectedFoodGroup = (AFoodGroup) myFoodGroupsAdapter
						.getItem(position);

				// Do something with value!
				ArrayList<Pair<Float, AFood>> mFoods = mADay.getFoods();
				ArrayList<Pair<Float, AFood>> selectedFoods = selectedFoodGroup
						.getFoods();
				mFoods.addAll(selectedFoods);
				mADay.setFoods(mFoods);
				application.saveDay(mADay);
				finish();
			}
		});
	}

	private void refreshList() {
		if (application.hasFoodGroupData(mContext)) {
			List<AFoodGroup> tempList = application.getFoodGroupsFromDb();
			myFoodGroupsArrayList = new ArrayList<AFoodGroup>();
			for (int i = 0; i < tempList.size(); i++) {
				myFoodGroupsArrayList.add(tempList.get(i));
			}
			myFoodGroupsAdapter = new MyFoodGroupsAdapter(mContext,
					myFoodGroupsArrayList);
			myFoodGroupsLV.setAdapter(myFoodGroupsAdapter);
		} else {
			// No data!
		}
	}
}
