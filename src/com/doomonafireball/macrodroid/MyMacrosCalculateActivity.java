package com.doomonafireball.macrodroid;

import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;

public class MyMacrosCalculateActivity extends FragmentActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_macros_calculate_activity);

		final ActionBar ab = getSupportActionBar();
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle(getResources().getString(R.string.My_Macros));
		ab.setSubtitle(getResources().getString(R.string.Calculate));
    }
    
    private void setMacros() {
    	// TODO This
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_macros_calculate_menu, menu);
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
