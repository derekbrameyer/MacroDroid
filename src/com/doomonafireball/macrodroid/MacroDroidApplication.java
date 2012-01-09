package com.doomonafireball.macrodroid;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class MacroDroidApplication extends Application {
	private static final String MACROS_PREFS = "MacroDroidMacrosPrefs";
	public SharedPreferences macrosPrefs;
	public SharedPreferences.Editor macrosPrefsEditor;
	private Db4oHelper dbHelper;

	@Override
	public void onCreate() {
		macrosPrefs = getSharedPreferences(MACROS_PREFS, 0);
		macrosPrefsEditor = macrosPrefs.edit();
		if (!macrosPrefs.contains(Tags.MACRO_CONVERSION_PROTEIN)) {
			macrosPrefsEditor.putFloat(Tags.MACRO_CONVERSION_PROTEIN,
					DefaultValues.MACRO_CONVERSION_PROTEIN);
			macrosPrefsEditor.commit();
		}
		if (!macrosPrefs.contains(Tags.MACRO_CONVERSION_CARBS)) {
			macrosPrefsEditor.putFloat(Tags.MACRO_CONVERSION_CARBS,
					DefaultValues.MACRO_CONVERSION_CARBS);
			macrosPrefsEditor.commit();
		}
		if (!macrosPrefs.contains(Tags.MACRO_CONVERSION_FAT)) {
			macrosPrefsEditor.putFloat(Tags.MACRO_CONVERSION_FAT,
					DefaultValues.MACRO_CONVERSION_FAT);
			macrosPrefsEditor.commit();
		}
	}

	public String getRandomCompliment() {
		String[] complimentArray = getResources().getStringArray(
				R.array.compliments);
		int i = (int) (Math.random() * complimentArray.length);
		return complimentArray[i];
	}

	public int numPicsForDay(String path, String day) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return 0;
		} else {
			File dir = new File(Environment.getExternalStorageDirectory()
					+ File.separator + path);
			dir.mkdirs();
			String[] files = dir.list();
			int dayCount = 0;
			for (int i = 0; i < files.length; i++) {
				String s = files[i];
				if (s.contains(day)) {
					dayCount++;
				}
			}
			return dayCount;

		}
	}

	public int directoryExists(String path) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return -1;
		} else {
			File dir = new File(Environment.getExternalStorageDirectory()
					+ File.separator + path);
			if (dir.exists()) {
				return dir.list().length;
			} else {
				return -1;
			}
		}
	}

	public String saveBitmapToFile(Bitmap bmp, String path, String filename) {
		// Make sure to declare WRITE_EXTERNAL_STORAGE permission
		// bmp - self-explanatory
		// path - e.g "myapp/firstfolder/secondfolder" (no leading or trailing
		// seps)
		// filename - e.g. "081012.png" (no leading seps)
		// Output would be "sdcard/myapp/firstfolder/secondfolder/081012.png"
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return "";
		} else {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);
			File dir = new File(Environment.getExternalStorageDirectory()
					+ File.separator + path);
			dir.mkdirs();
			File f = new File(Environment.getExternalStorageDirectory()
					+ File.separator + path + File.separator + filename);
			Log.d(Tags.LOG_TAG, "Filedir: " + f.getAbsolutePath());
			try {
				// write the bytes in file
				FileOutputStream fo = new FileOutputStream(f);
				fo.write(bytes.toByteArray());
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
						Uri.parse("file://"
								+ Environment.getExternalStorageDirectory())));
				return f.getAbsolutePath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}
	}

	public boolean hasDayData(Context ctx) {
		dbHelper = new Db4oHelper(ctx);
		if (dbHelper.fetchAllDayRows().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public boolean hasFoodGroupData(Context ctx) {
		dbHelper = new Db4oHelper(ctx);
		if (dbHelper.fetchAllFoodGroupRows() == null) {
			return true;
		}
		if (dbHelper.fetchAllFoodGroupRows().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public void deleteDatabase() {
		dbHelper.deleteDatabase();
	}

	public void saveDay(ADay day) {
		Log.d(Tags.LOG_TAG, "Saving day: \n" + day.toString());
		dbHelper.saveDay(day);
	}

	public void saveFoodGroup(AFoodGroup foodGroup) {
		Log.d(Tags.LOG_TAG, "Saving food group: \n" + foodGroup.toString());
		dbHelper.saveFoodGroup(foodGroup);
	}

	public void deleteFoodGroup(AFoodGroup foodGroup) {
		dbHelper.deleteFoodGroup(foodGroup.id);
	}

	public List<ADay> getDaysFromDb() {
		return dbHelper.fetchAllDayRows();
	}

	public List<AFoodGroup> getFoodGroupsFromDb() {
		return dbHelper.fetchAllFoodGroupRows();
	}

	public List<ADay> getADayForCalendar(Calendar cal) {
		return dbHelper.fetchFoodsForDate(cal);
	}

	public ADay safelyFetchExistingADay(Calendar calendar) {
		if (hasDayData(this)) {
			// Try to find the ADay object
			List<ADay> possibleCurrDays = getADayForCalendar(calendar);
			if (!possibleCurrDays.isEmpty()) {
				// We have a current object
				Log.d(Tags.LOG_TAG, "We have a current day for today.");
				return possibleCurrDays.get(0);
			} else {
				// Instantiate a new object
				Log.d(Tags.LOG_TAG, "We don't have a current day for today.");
				ADay retDay = new ADay(calendar, true);
				saveDay(retDay);
				return retDay;
			}
		} else {
			// Instantiate a new object
			Log.d(Tags.LOG_TAG,
					"We don't even have data! AND We don't have a current day.");
			ADay retDay = new ADay(calendar, true);
			saveDay(retDay);
			return retDay;
		}
	}

	public ArrayList<AFood> getAllFoods() {
		byte[] allFoodsByteArray = readInternalStoragePrivate(Tags.MY_FOODS_FILENAME);
		String allFoodsRaw = new String(allFoodsByteArray);
		String[] allFoods = allFoodsRaw.split("\\r?\\n");

		// Parse the String array into an ArrayList<AFood>
		ArrayList<AFood> allFoodsArrayList = new ArrayList<AFood>();
		if (allFoods != null) {
			if (allFoods.length > 5) {
				for (int i = 0; i < allFoods.length; i += 7) {
					AFood food = new AFood();

					Log.d(Tags.LOG_TAG, "Name: " + allFoods[i]);
					Log.d(Tags.LOG_TAG, "Serving size: " + allFoods[i + 1]);
					Log.d(Tags.LOG_TAG, "kCal: " + allFoods[i + 2]);
					Log.d(Tags.LOG_TAG, "Protein: " + allFoods[i + 3]);
					Log.d(Tags.LOG_TAG, "Carbs: " + allFoods[i + 4]);
					Log.d(Tags.LOG_TAG, "Fat: " + allFoods[i + 5]);

					food.setFoodName(allFoods[i]);
					food.setFoodServingSize(allFoods[i + 1]);
					food.setFoodKCal(Float.parseFloat(allFoods[i + 2]));
					food.setFoodGramsProtein(Float.parseFloat(allFoods[i + 3]));
					food.setFoodGramsCarbs(Float.parseFloat(allFoods[i + 4]));
					food.setFoodGramsFat(Float.parseFloat(allFoods[i + 5]));

					allFoodsArrayList.add(food);
				}
			}
		}

		return allFoodsArrayList;
	}

	public byte[] readInternalStoragePrivate(String filename) {
		int len = 1024;
		byte[] buffer = new byte[len];
		try {
			FileInputStream fis = openFileInput(filename);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int nrb = fis.read(buffer, 0, len);
			while (nrb != -1) {
				baos.write(buffer, 0, nrb);
				nrb = fis.read(buffer, 0, len);
			}
			buffer = baos.toByteArray();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO If not found, write the default_foods.txt res/raw file to it
			e.printStackTrace();
			writeInternalStoragePrivate(Tags.MY_FOODS_FILENAME,
					defaultFoodsToByteArray());
			readInternalStoragePrivate(Tags.MY_FOODS_FILENAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public void writeInternalStoragePrivate(String filename, byte[] content) {
		try {
			FileOutputStream fos = openFileOutput(filename,
					Context.MODE_PRIVATE);
			fos.write(content);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeFoodsToInternalStorage(ArrayList<AFood> foods) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (int i = 0; i < foods.size(); i++) {
				AFood f = foods.get(i);
				Log.d(Tags.LOG_TAG, "Writing Name: " + f.getFoodName());
				Log.d(Tags.LOG_TAG,
						"Writing Serving size: " + f.getFoodServingSize());
				Log.d(Tags.LOG_TAG, "Writing kCal: " + f.getFoodKCal());
				Log.d(Tags.LOG_TAG,
						"Writing Protein: " + f.getFoodGramsProtein());
				Log.d(Tags.LOG_TAG, "Writing Carbs: " + f.getFoodGramsCarbs());
				Log.d(Tags.LOG_TAG, "Writing Fat: " + f.getFoodGramsFat());
				baos.write(foodToByteArray(f));
			}
			byte[] newFoodList = baos.toByteArray();
			writeInternalStoragePrivate(Tags.MY_FOODS_FILENAME, newFoodList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addFoodToInternalStorage(AFood food) {
		try {
			byte[] retChar = "\n".getBytes();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(readInternalStoragePrivate(Tags.MY_FOODS_FILENAME));
			baos.write(foodToByteArray(food));
			byte[] newFoodList = baos.toByteArray();
			writeInternalStoragePrivate(Tags.MY_FOODS_FILENAME, newFoodList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public byte[] foodToByteArray(AFood food) {
		try {
			byte[] retChar = "\n".getBytes();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(food.getFoodName().getBytes());
			baos.write(retChar);
			baos.write(food.getFoodServingSize().getBytes());
			baos.write(retChar);
			baos.write(Float.toString(food.getFoodKCal()).getBytes());
			baos.write(retChar);
			baos.write(Float.toString(food.getFoodGramsProtein()).getBytes());
			baos.write(retChar);
			baos.write(Float.toString(food.getFoodGramsCarbs()).getBytes());
			baos.write(retChar);
			baos.write(Float.toString(food.getFoodGramsFat()).getBytes());
			baos.write(retChar);
			baos.write(retChar);
			return baos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public byte[] defaultFoodsToByteArray() {
		int len = 1024;
		byte[] buffer = new byte[len];
		try {
			InputStream fis = getResources().openRawResource(
					R.raw.default_foods);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int nrb = fis.read(buffer, 0, len);
			while (nrb != -1) {
				baos.write(buffer, 0, nrb);
				nrb = fis.read(buffer, 0, len);
			}
			buffer = baos.toByteArray();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
}
