package com.doomonafireball.macrodroid;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class MacroDroidApplication extends Application {
    private static final String MACROS_PREFS = "MacroDroidMacrosPrefs";
    public SharedPreferences macrosPrefs;
    public SharedPreferences.Editor macrosPrefsEditor;

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
            writeInternalStoragePrivate(Tags.MY_FOODS_FILENAME, defaultFoodsToByteArray());
            readInternalStoragePrivate(Tags.MY_FOODS_FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public void writeInternalStoragePrivate(
            String filename, byte[] content) {
        try {
            FileOutputStream fos =
                openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(content);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public byte[] defaultFoodsToByteArray() {
        int len = 1024;
        byte[] buffer = new byte[len];
        try {
            InputStream fis = getResources().openRawResource(R.raw.default_foods);
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
