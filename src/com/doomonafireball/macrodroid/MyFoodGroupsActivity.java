package com.doomonafireball.macrodroid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MyFoodGroupsActivity extends Activity {
	private Button addBTN;
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

		addBTN = (Button) findViewById(R.id.BTN_add);
		myFoodGroupsLV = (ListView) findViewById(R.id.LV_my_food_groups);

		addBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// startActivity(new Intent(MyFoodGroupsActivity.this,
				// MyFoodGroupsAddActivity.class));
			}
		});

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
}
