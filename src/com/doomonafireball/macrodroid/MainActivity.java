package com.doomonafireball.macrodroid;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.exina.android.calendar.CalendarActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	private Button todayBTN;
	private Button previousBTN;
	private Button myFoodsBTN;
	private Button myMacrosBTN;
	private Button myFoodGroupsBTN;
	private Button settingsBTN;
	private Context mContext;
	private MacroDroidApplication application;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		mContext = this;
		application = ((MacroDroidApplication) getApplication());

		final ActionBar ab = getSupportActionBar();
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		ab.setSubtitle(application.getRandomCompliment());

		todayBTN = (Button) findViewById(R.id.BTN_today);
		previousBTN = (Button) findViewById(R.id.BTN_previous);
		myFoodsBTN = (Button) findViewById(R.id.BTN_my_foods);
		myMacrosBTN = (Button) findViewById(R.id.BTN_my_macros);
		myFoodGroupsBTN = (Button) findViewById(R.id.BTN_my_food_groups);
		settingsBTN = (Button) findViewById(R.id.BTN_settings);

		todayBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Convert current date and pass it to intent
				Intent i = new Intent(MainActivity.this,
						DayDetailActivity.class);
				i.putExtra("date", Calendar.getInstance().get(Calendar.DATE));
				i.putExtra("month", Calendar.getInstance().get(Calendar.MONTH));
				i.putExtra("year", Calendar.getInstance().get(Calendar.YEAR));
				startActivity(i);
			}
		});
		previousBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Calendar Widget
				startActivityForResult(new Intent(Intent.ACTION_PICK)
						.setDataAndType(null, CalendarActivity.MIME_TYPE), 100);

				/*
				// DatePickerDialog
				int year = Calendar.getInstance().get(Calendar.YEAR);
				int month = Calendar.getInstance().get(Calendar.MONTH);
				int date = Calendar.getInstance().get(Calendar.DATE);
				DatePickerDialog datePicker = new DatePickerDialog(mContext,
						new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int month, int date) {
								// Convert current date and pass it to intent
								Intent i = new Intent(MainActivity.this,
										DayDetailActivity.class);
								i.putExtra("date", date);
								i.putExtra("month", month);
								i.putExtra("year", year);
								startActivity(i);
							}

						}, year, month, date);
				datePicker.show();*/
			}
		});
		myFoodsBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						MyFoodsActivity.class));
			}
		});
		myMacrosBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						MyMacrosActivity.class));
			}
		});
		myFoodGroupsBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						MyFoodGroupsActivity.class));
			}
		});
		settingsBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			int year = data.getIntExtra("year", 0); // get number of year
			int month = data.getIntExtra("month", 0); // get number of month
														// 0..11
			int day = data.getIntExtra("day", 0); // get number of day 0..31

			Intent i = new Intent(MainActivity.this, DayDetailActivity.class);
			i.putExtra("date", day);
			i.putExtra("month", month);
			i.putExtra("year", year);
			startActivity(i);
		}
	}
	
	@Override
	public void onResume() {
		final ActionBar ab = getSupportActionBar();
		ab.setSubtitle(application.getRandomCompliment());
		super.onResume();
	}
}