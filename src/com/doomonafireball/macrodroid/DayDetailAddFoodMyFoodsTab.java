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

		mADay = application.safelyFetchExistingADay(mCalendar);
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
								ArrayList<Pair<Float, AFood>> mFoods = mADay.getFoods();
								if (mFoods == null) {
									mFoods = new ArrayList<Pair<Float, AFood>>();
								}
								mFoods.add(new Pair<Float, AFood>(servingsValue, selectedFood));
								mADay.setFoods(mFoods);
								application.saveDay(mADay);
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

	private void refreshList() {
		myFoodsArrayList = application.getAllFoods();
		myFoodsAdapter = new MyFoodsAdapter(mContext, myFoodsArrayList);
		myFoodsLV.setAdapter(myFoodsAdapter);
	}
}
