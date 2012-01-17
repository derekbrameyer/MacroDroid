package com.doomonafireball.macrodroid;

import java.util.Calendar;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class DayDetailAddFoodTabActivity extends TabActivity {
	private MacroDroidApplication application;
	private Context mContext;
	private Calendar mCalendar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.day_detail_add_food_tab_activity);

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

		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, DayDetailAddFoodMyFoodsTab.class);
		intent.putExtras(extras);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost
				.newTabSpec("my_foods")
				.setIndicator("My Foods")
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this,
				DayDetailAddFoodMyFoodGroupsTab.class);
		intent.putExtras(extras);

		spec = tabHost
				.newTabSpec("my_food_groups")
				.setIndicator("Food Groups")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, DayDetailAddFoodNewFoodTab.class);
		intent.putExtras(extras);

		spec = tabHost
				.newTabSpec("new_food")
				.setIndicator("New Food")
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
	}
}
