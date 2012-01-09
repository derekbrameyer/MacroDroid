package com.doomonafireball.macrodroid;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MyFoodsActivity extends FragmentActivity {
	private ListView myFoodsLV;
	private MyFoodsAdapter myFoodsAdapter;
	private MacroDroidApplication application;
	private Context mContext;
	private ArrayList<AFood> myFoodsArrayList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_foods_activity);

		application = ((MacroDroidApplication) getApplication());
		mContext = this;

		final ActionBar ab = getSupportActionBar();
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle(getResources().getString(R.string.My_Foods));

		myFoodsLV = (ListView) findViewById(R.id.LV_my_foods);

		refreshList();

		myFoodsLV.setOnItemLongClickListener(myFoodsItemLongClickListener);
	}

	private OnItemLongClickListener myFoodsItemLongClickListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> a, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			final AFood selectedFood = (AFood) myFoodsAdapter.getItem(position);

			final CharSequence[] items = { "Delete" };
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle("Options");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					if (item == 0) {
						// Delete this food
						myFoodsArrayList.remove(selectedFood);
						application
								.writeFoodsToInternalStorage(myFoodsArrayList);
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
		myFoodsArrayList = application.getAllFoods();
		myFoodsAdapter = new MyFoodsAdapter(mContext, myFoodsArrayList);
		myFoodsLV.setAdapter(myFoodsAdapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_foods_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.MENU_add_food:
			startActivity(new Intent(MyFoodsActivity.this,
					MyFoodsAddActivity.class));
			return false;
		case R.id.MENU_import:
			// TODO Import from file
			application.writeInternalStoragePrivate(Tags.MY_FOODS_FILENAME,
					application.defaultFoodsToByteArray());
			refreshList();
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
