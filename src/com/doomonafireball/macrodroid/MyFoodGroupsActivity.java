package com.doomonafireball.macrodroid;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MyFoodGroupsActivity extends FragmentActivity {
	private ListView myFoodGroupsLV;
	private MyFoodGroupsAdapter myFoodGroupsAdapter;
	private MacroDroidApplication application;
	private Context mContext;
	private ArrayList<AFoodGroup> myFoodGroupsArrayList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_food_groups_activity);

		application = ((MacroDroidApplication) getApplication());
		mContext = this;
		
		final ActionBar ab = getSupportActionBar();
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle(getResources().getString(R.string.My_Food_Groups));
		
		myFoodGroupsLV = (ListView) findViewById(R.id.LV_my_food_groups);

		refreshList();

		myFoodGroupsLV.setOnItemLongClickListener(myFoodsItemLongClickListener);
	}

	private OnItemLongClickListener myFoodsItemLongClickListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> a, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			final AFoodGroup selectedFoodGroup = (AFoodGroup) myFoodGroupsAdapter
					.getItem(position);

			final CharSequence[] items = { "Delete" };
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle("Options");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					if (item == 0) {
						// Delete this food
						application.deleteFoodGroup(selectedFoodGroup);
						refreshList();
						dialog.dismiss();
					}
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
			return false;
		}
	};

	private void refreshList() {
		if (application.hasFoodGroupData(mContext)) {
			List<AFoodGroup> tempList = application
					.getFoodGroupsFromDb();
			myFoodGroupsArrayList = new ArrayList<AFoodGroup>();
			for (int i = 0; i < tempList.size(); i++) {
				myFoodGroupsArrayList.add(tempList.get(i));
			}
			myFoodGroupsAdapter = new MyFoodGroupsAdapter(mContext,
					myFoodGroupsArrayList);
			myFoodGroupsLV.setAdapter(myFoodGroupsAdapter);
		} else {
			// No data!
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_food_groups_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.MENU_add_food_group:
			// TODO
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
