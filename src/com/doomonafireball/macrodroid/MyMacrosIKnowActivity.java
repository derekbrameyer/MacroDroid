package com.doomonafireball.macrodroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyMacrosIKnowActivity extends Activity {
    private MacroDroidApplication application;
    private Button setBTN;
    private Button cancelBTN;
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

        setBTN = (Button) findViewById(R.id.BTN_set);
        cancelBTN = (Button) findViewById(R.id.BTN_cancel);
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
        trainingKCalET.setText(Float.toString(application.macrosPrefs.getFloat(
                Tags.MACROS_TRAINING_KCAL, 0)));
        restKCalET.setText(Float.toString(application.macrosPrefs
                .getFloat(Tags.MACROS_REST_KCAL, 0)));
        deficitET.setText(Float.toString(application.macrosPrefs.getFloat(Tags.MACROS_DEFICIT, 0.0f)));
        proteinET.setText(Float.toString(application.macrosPrefs.getFloat(Tags.MACROS_PROTEIN, 0.0f)));
        carbsET.setText(Float.toString(application.macrosPrefs.getFloat(Tags.MACROS_CARBS, 0.0f)));
        fatET.setText(Float.toString(application.macrosPrefs.getFloat(Tags.MACROS_FAT, 0.0f)));
        proteinConversionET.setText(Float.toString(application.macrosPrefs.getFloat(
                Tags.MACRO_CONVERSION_PROTEIN,
                DefaultValues.MACRO_CONVERSION_PROTEIN)));
        carbsConversionET.setText(Float.toString(application.macrosPrefs.getFloat(
                Tags.MACRO_CONVERSION_CARBS,
                DefaultValues.MACRO_CONVERSION_CARBS)));
        fatConversionET.setText(Float.toString(application.macrosPrefs.getFloat(
                Tags.MACRO_CONVERSION_FAT,
                DefaultValues.MACRO_CONVERSION_FAT)));

        setBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        cancelBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Auto-generated method stub
                finish();
            }
        });
    }
}
