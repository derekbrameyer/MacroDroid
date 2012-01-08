package com.doomonafireball.macrodroid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class DayDetailActivity extends Activity {
	private static final String DATE_FORMAT = "MM/dd/yyyy";
	private MacroDroidApplication application;
	private Context mContext;
	private Calendar mCalendar; // purely for setup
	private ADay mADay;
	private TextView dateTV;
	private Button trainingBTN;
	private TextView noFoodsYetTV;
	private TextView caloriesTV;
	private TextView currentProteinTV;
	private TextView currentCarbsTV;
	private TextView currentFatTV;
	private TextView macroProteinTV;
	private TextView macroCarbsTV;
	private TextView macroFatTV;
	private Button picsBTN;
	private Button weightBTN;
	private Button heightBTN;
	private Button addFoodBTN;
	private DayDetailFoodsAdapter mFoodsAdapter;
	private ListView mFoodsLV;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.day_detail_activity);

		application = ((MacroDroidApplication) getApplication());
		mContext = this;

		noFoodsYetTV = (TextView) findViewById(R.id.TV_no_foods_yet);
		dateTV = (TextView) findViewById(R.id.TV_day_detail_date);
		trainingBTN = (Button) findViewById(R.id.BTN_training_day);
		caloriesTV = (TextView) findViewById(R.id.TV_day_detail_calories);
		currentProteinTV = (TextView) findViewById(R.id.TV_day_detail_current_protein);
		currentCarbsTV = (TextView) findViewById(R.id.TV_day_detail_current_carbs);
		currentFatTV = (TextView) findViewById(R.id.TV_day_detail_current_fat);
		macroProteinTV = (TextView) findViewById(R.id.TV_day_detail_macro_protein);
		macroCarbsTV = (TextView) findViewById(R.id.TV_day_detail_macro_carbs);
		macroFatTV = (TextView) findViewById(R.id.TV_day_detail_macro_fat);
		picsBTN = (Button) findViewById(R.id.BTN_pics);
		weightBTN = (Button) findViewById(R.id.BTN_weight);
		heightBTN = (Button) findViewById(R.id.BTN_height);
		addFoodBTN = (Button) findViewById(R.id.BTN_add_food);
		mFoodsLV = (ListView) findViewById(R.id.LV_day_detail_foods);

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
			Log.d(Tags.LOG_TAG, "This day is: " + mMonth + "/" + mDate + "/"
					+ mYear);
		} else {
			// TODO Something went wrong (this should never happen)
		}
	}

	OnItemClickListener mFoodsItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> av, View v, int position, long id) {
			// TODO Auto-generated method stub
			ArrayList<Pair<Float, AFood>> mFoods = mADay.getFoods();
			mFoods.remove(position);
			mADay.setFoods(mFoods);
			application.saveDay(mADay);
			refreshViews();
		}
	};

	private void renderDetailInfo() {
		// First, fetch existing ADay object
		mADay = application.safelyFetchExistingADay(mCalendar);

		refreshViews();
	}

	private void refreshViews() {
		// Refresh the date!
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		dateTV.setText(sdf.format(mADay.getDate().getTime()));

		// Refresh the Views
		float totalKCal = 0.0f;
		float totalProtein = 0.0f;
		float totalCarbs = 0.0f;
		float totalFat = 0.0f;

		ArrayList<Pair<Float, AFood>> mFoods = mADay.getFoods();
		if (mFoods != null) {
			if (mFoods.isEmpty()) {
				noFoodsYetTV.setVisibility(View.VISIBLE);
			} else {
				noFoodsYetTV.setVisibility(View.GONE);
				mFoodsAdapter = new DayDetailFoodsAdapter(mContext, mFoods);
				mFoodsLV.setAdapter(mFoodsAdapter);

				for (int i = 0; i < mFoods.size(); i++) {
					Pair<Float, AFood> pair = mFoods.get(i);
					Float servings = pair.first;
					AFood food = pair.second;

					totalKCal += (servings * food.getFoodKCal());
					totalProtein += (servings * food.getFoodGramsProtein());
					totalCarbs += (servings * food.getFoodGramsCarbs());
					totalFat += (servings * food.getFoodGramsFat());
				}
			}
		} else {
			noFoodsYetTV.setVisibility(View.VISIBLE);
		}

		caloriesTV.setText(Float.toString(totalKCal)
				+ "/"
				+ Float.toString(application.macrosPrefs.getFloat(
						Tags.MACROS_TRAINING_KCAL, 0.0f)) + " kCal");
		currentProteinTV.setText(Float.toString(totalProtein));
		currentCarbsTV.setText(Float.toString(totalCarbs));
		currentFatTV.setText(Float.toString(totalFat));
		macroProteinTV.setText("of "
				+ Float.toString(application.macrosPrefs.getFloat(
						Tags.MACROS_PROTEIN, 0.0f)) + "g");
		macroCarbsTV.setText("of "
				+ Float.toString(application.macrosPrefs.getFloat(
						Tags.MACROS_CARBS, 0.0f)) + "g");
		macroFatTV.setText("of "
				+ Float.toString(application.macrosPrefs.getFloat(
						Tags.MACROS_FAT, 0.0f)) + "g");

		// Color text red if over
		if (totalKCal > application.macrosPrefs.getFloat(
				Tags.MACROS_TRAINING_KCAL, 0.0f)) {
			caloriesTV.setTextColor(Color.RED);
		}
		if (totalProtein > application.macrosPrefs.getFloat(
				Tags.MACROS_PROTEIN, 0.0f)) {
			currentProteinTV.setTextColor(Color.RED);
		}
		if (totalCarbs > application.macrosPrefs.getFloat(Tags.MACROS_CARBS,
				0.0f)) {
			currentCarbsTV.setTextColor(Color.RED);
		}
		if (totalFat > application.macrosPrefs.getFloat(Tags.MACROS_FAT, 0.0f)) {
			currentFatTV.setTextColor(Color.RED);
		}

		// Buttons
		weightBTN.setText(Float.toString(mADay.getWeight()));
		weightBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
				alert.setTitle("Weight");
				// Set an EditText view to get user input
				final EditText input = new EditText(mContext);
				input.setText("100.0");
				alert.setView(input);
				alert.setPositiveButton("Set",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Float weight = Float.parseFloat(input.getText()
										.toString());
								mADay.setWeight(weight);
								application.saveDay(mADay);
								dialog.dismiss();
								renderDetailInfo();
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
		heightBTN.setText(Float.toString(mADay.getHeight()));
		heightBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
				alert.setTitle("Height");
				// Set an EditText view to get user input
				final EditText input = new EditText(mContext);
				input.setText("100.0");
				alert.setView(input);
				alert.setPositiveButton("Set",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Float height = Float.parseFloat(input.getText()
										.toString());
								mADay.setHeight(height);
								application.saveDay(mADay);
								dialog.dismiss();
								renderDetailInfo();
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
		addFoodBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Convert current date and pass it to intent
				Intent i = new Intent(DayDetailActivity.this,
						DayDetailAddFoodTabActivity.class);
				i.putExtra("date", mADay.getDate().get(Calendar.DATE));
				i.putExtra("month", mADay.getDate().get(Calendar.MONTH));
				i.putExtra("year", mADay.getDate().get(Calendar.YEAR));
				startActivity(i);
			}
		});
		if (mADay.isTraining()) {
			trainingBTN.setText("Training Day");
		} else {
			trainingBTN.setText("Rest Day");
		}
		trainingBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mADay.isTraining()) {
					mADay.setTraining(false);
					trainingBTN.setText("Rest Day");
					application.saveDay(mADay);
				} else {
					mADay.setTraining(true);
					trainingBTN.setText("Training Day");
					application.saveDay(mADay);
				}
			}
		});
		mFoodsLV.setOnItemClickListener(mFoodsItemClickListener);
	}

	private void saveToFoodGroup() {
		AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
		alert.setTitle("Food Group Name");
		// Set an EditText view to get user input
		final EditText input = new EditText(mContext);
		input.setText("");
		alert.setView(input);
		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String name = input.getText().toString();
				AFoodGroup foodGroup = new AFoodGroup(name, mADay.getFoods());
				application.saveFoodGroup(foodGroup);
				dialog.dismiss();
			}
		});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				});
		alert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.day_detail_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.MENU_save_food_group:
			saveToFoodGroup();
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		renderDetailInfo();
	}
}
