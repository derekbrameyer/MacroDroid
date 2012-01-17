package com.doomonafireball.macrodroid;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DayDetailActivity extends FragmentActivity {
	private static final String DATE_FORMAT = "MM/dd/yyyy";
	private static final String SAVE_DATE_FORMAT = "MMddyyyy";
	private static final int CAMERA_PIC_REQUEST = 1337;
	private MacroDroidApplication application;
	private Context mContext;
	private Calendar mCalendar; // purely for setup
	private ADay mADay;
	private TextView noFoodsYetTV;
	private TextView caloriesTV;
	private TextView currentProteinTV;
	private TextView currentCarbsTV;
	private TextView currentFatTV;
	private TextView macroProteinTV;
	private TextView macroCarbsTV;
	private TextView macroFatTV;
	private DayDetailFoodsAdapter mFoodsAdapter;
	private ListView mFoodsLV;
	private String mFilePath;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.day_detail_activity);

		application = ((MacroDroidApplication) getApplication());
		mContext = this;

		final ActionBar ab = getSupportActionBar();
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowHomeEnabled(false);

		noFoodsYetTV = (TextView) findViewById(R.id.TV_no_foods_yet);
		caloriesTV = (TextView) findViewById(R.id.TV_day_detail_calories);
		currentProteinTV = (TextView) findViewById(R.id.TV_day_detail_current_protein);
		currentCarbsTV = (TextView) findViewById(R.id.TV_day_detail_current_carbs);
		currentFatTV = (TextView) findViewById(R.id.TV_day_detail_current_fat);
		macroProteinTV = (TextView) findViewById(R.id.TV_day_detail_macro_protein);
		macroCarbsTV = (TextView) findViewById(R.id.TV_day_detail_macro_carbs);
		macroFatTV = (TextView) findViewById(R.id.TV_day_detail_macro_fat);
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
			// TODO Quick Options
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
		final ActionBar ab = getSupportActionBar();
		ab.setTitle(sdf.format(mADay.getDate().getTime()));
		// dateTV.setText(sdf.format(mADay.getDate().getTime()));

		SimpleDateFormat sdf2 = new SimpleDateFormat(SAVE_DATE_FORMAT);
		mFilePath = sdf2.format(mADay.getDate().getTime());

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
		} else {
			caloriesTV.setTextColor(Color.GRAY);
		}
		if (totalProtein > application.macrosPrefs.getFloat(
				Tags.MACROS_PROTEIN, 0.0f)) {
			currentProteinTV.setTextColor(Color.RED);
		} else {
			currentProteinTV.setTextColor(Color.GRAY);
		}
		if (totalCarbs > application.macrosPrefs.getFloat(Tags.MACROS_CARBS,
				0.0f)) {
			currentCarbsTV.setTextColor(Color.RED);
		} else {
			currentCarbsTV.setTextColor(Color.GRAY);
		}
		if (totalFat > application.macrosPrefs.getFloat(Tags.MACROS_FAT, 0.0f)) {
			currentFatTV.setTextColor(Color.RED);
		} else {
			currentFatTV.setTextColor(Color.GRAY);
		}

		// Buttons
		// /weightBTN.setText(Float.toString(mADay.getWeight()));

		if (mADay.isTraining()) {
			ab.setSubtitle(getResources().getString(R.string.Training_Day));
		} else {
			ab.setSubtitle(getResources().getString(R.string.Rest_Day));
		}
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_PIC_REQUEST) {
			Log.d(Tags.LOG_TAG, "CAMERA_PIC_REQUEST result.");
			if (data.getExtras() != null) {
				Bitmap bmp = (Bitmap) data.getExtras().get("data");
				String saveBmpResults = application.saveBitmapToFile(
						bmp,
						Tags.PHOTOS_PATH,
						mFilePath
								+ "_"
								+ application.numPicsForDay(Tags.PHOTOS_PATH,
										mFilePath) + ".png");
				if (saveBmpResults.length() > 0) {
					Toast.makeText(mContext,
							"Saved picture to " + saveBmpResults,
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(
							mContext,
							"Could not save the picture. Please make sure your SD card is mounted.",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.day_detail_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.MENU_photos:
			if (application.numPicsForDay(Tags.PHOTOS_PATH, mFilePath) > 0) {
				// We already have some pictures for this day
				final CharSequence[] items = { "View photos",
						"Take another photo" };
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Choose an option");
				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						if (item == 0) {
							Intent intent = new Intent();
							intent.setAction(Intent.ACTION_VIEW);
							intent.setDataAndType(
									Uri.parse("file://" + File.separator
											+ Tags.PHOTOS_PATH), "image/*");
							startActivity(intent);

						} else {
							Intent cameraIntent = new Intent(
									android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(cameraIntent,
									CAMERA_PIC_REQUEST);
						}
					}
				});
				builder.setNegativeButton("Cancel", new OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			} else {
				// We have no pictures for this day
				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
			}
			return false;
		case R.id.MENU_weight:
			AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
			alert.setTitle("Set Weight");
			// Set an EditText view to get user input
			final EditText input = new EditText(mContext);
			input.append("100.0");
			alert.setView(input);
			alert.setPositiveButton("Set Weight",
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
			return false;
		case R.id.MENU_add_food:
			Intent i = new Intent(DayDetailActivity.this,
					DayDetailAddFoodTabActivity.class);
			i.putExtra("date", mADay.getDate().get(Calendar.DATE));
			i.putExtra("month", mADay.getDate().get(Calendar.MONTH));
			i.putExtra("year", mADay.getDate().get(Calendar.YEAR));
			startActivity(i);
			return false;
		case R.id.MENU_save_food_group:
			saveToFoodGroup();
			return false;
		case R.id.MENU_change_day_type:
			final ActionBar ab = getSupportActionBar();
			if (mADay.isTraining()) {
				mADay.setTraining(false);
				ab.setSubtitle(getResources().getString(R.string.Rest_Day));
				application.saveDay(mADay);
			} else {
				mADay.setTraining(true);
				ab.setSubtitle(getResources().getString(R.string.Training_Day));
				application.saveDay(mADay);
			}
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
