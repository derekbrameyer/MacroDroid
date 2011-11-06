package com.doomonafireball.macrodroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class DayDetailAddFoodNewFoodTab extends Activity {
	private MacroDroidApplication application;
	private Context mContext;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.day_detail_add_food_new_food_tab);

		application = ((MacroDroidApplication) getApplication());
		mContext = this;
	}
}
