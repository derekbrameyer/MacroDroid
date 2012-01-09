package com.doomonafireball.macrodroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyMacrosIKnowActivity extends FragmentActivity {
    private MacroDroidApplication application;
    private EditText trainingKCalET;
    private EditText restKCalET;
    private EditText deficitET;
    private EditText proteinET;
    private EditText carbsET;
    private EditText fatET;
    private EditText proteinConversionET;
    private EditText carbsConversionET;
    private EditText fatConversionET;
    private Context mContext;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_macros_i_know_activity);

        application = ((MacroDroidApplication) getApplication());
        mContext = this;

		final ActionBar ab = getSupportActionBar();
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle(getResources().getString(R.string.My_Macros));
		ab.setSubtitle(getResources().getString(R.string.I_Know_Them));
		
        trainingKCalET = (EditText) findViewById(R.id.ET_training_kCal);
        restKCalET = (EditText) findViewById(R.id.ET_rest_kCal);
        deficitET = (EditText) findViewById(R.id.ET_deficit);
        proteinET = (EditText) findViewById(R.id.ET_protein);
        carbsET = (EditText) findViewById(R.id.ET_carbs);
        fatET = (EditText) findViewById(R.id.ET_fat);
        proteinConversionET = (EditText) findViewById(R.id.ET_protein_conversion);
        carbsConversionET = (EditText) findViewById(R.id.ET_carbs_conversion);
        fatConversionET = (EditText) findViewById(R.id.ET_fat_conversion);

        // Populate existing data
        trainingKCalET.append(Float.toString(application.macrosPrefs.getFloat(
                Tags.MACROS_TRAINING_KCAL, 0)));
        restKCalET.append(Float.toString(application.macrosPrefs
                .getFloat(Tags.MACROS_REST_KCAL, 0)));
        deficitET.append(Float.toString(application.macrosPrefs.getFloat(Tags.MACROS_DEFICIT, 0.0f)));
        proteinET.append(Float.toString(application.macrosPrefs.getFloat(Tags.MACROS_PROTEIN, 0.0f)));
        carbsET.append(Float.toString(application.macrosPrefs.getFloat(Tags.MACROS_CARBS, 0.0f)));
        fatET.append(Float.toString(application.macrosPrefs.getFloat(Tags.MACROS_FAT, 0.0f)));
        proteinConversionET.append(Float.toString(application.macrosPrefs.getFloat(
                Tags.MACRO_CONVERSION_PROTEIN,
                DefaultValues.MACRO_CONVERSION_PROTEIN)));
        carbsConversionET.append(Float.toString(application.macrosPrefs.getFloat(
                Tags.MACRO_CONVERSION_CARBS,
                DefaultValues.MACRO_CONVERSION_CARBS)));
        fatConversionET.append(Float.toString(application.macrosPrefs.getFloat(
                Tags.MACRO_CONVERSION_FAT,
                DefaultValues.MACRO_CONVERSION_FAT)));
    }
    
    private void setMacros() {
        // TODO Validate data entry
        application.macrosPrefsEditor.putFloat(Tags.MACROS_TRAINING_KCAL,
                Float.parseFloat(trainingKCalET.getText()
                        .toString()));
        application.macrosPrefsEditor.putFloat(Tags.MACROS_REST_KCAL,
                Float.parseFloat(restKCalET.getText()
                        .toString()));
        application.macrosPrefsEditor.putFloat(Tags.MACROS_DEFICIT,
                Float.parseFloat(deficitET.getText()
                        .toString()));
        application.macrosPrefsEditor.putFloat(Tags.MACROS_PROTEIN,
                Float.parseFloat(proteinET.getText()
                        .toString()));
        application.macrosPrefsEditor.putFloat(Tags.MACROS_CARBS,
                Float.parseFloat(carbsET.getText()
                        .toString()));
        application.macrosPrefsEditor.putFloat(Tags.MACROS_FAT,
                Float.parseFloat(fatET.getText()
                        .toString()));
        application.macrosPrefsEditor.putFloat(Tags.MACRO_CONVERSION_PROTEIN,
                Float.parseFloat(proteinConversionET.getText()
                        .toString()));
        application.macrosPrefsEditor.putFloat(Tags.MACRO_CONVERSION_CARBS,
                Float.parseFloat(carbsConversionET.getText()
                        .toString()));
        application.macrosPrefsEditor.putFloat(Tags.MACRO_CONVERSION_FAT,
                Float.parseFloat(fatConversionET.getText()
                        .toString()));
        application.macrosPrefsEditor.commit();
        Toast.makeText(mContext,
                getResources().getString(R.string.Your_macros_have_been_saved),
                Toast.LENGTH_SHORT).show();
        finish();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_macros_i_know_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.MENU_cancel:
			finish();
			return false;
		case R.id.MENU_set:
			setMacros();
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
