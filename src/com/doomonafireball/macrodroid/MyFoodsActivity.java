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
			AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
			alert.setTitle("Import from file");
			alert.setMessage(getResources().getString(
					R.string.import_instructions));
			alert.setCancelable(true);
			alert.setIcon(getResources()
					.getDrawable(R.drawable.ic_menu_archive));
			alert.setPositiveButton("Import",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							application.writeInternalStoragePrivate(
									Tags.MY_FOODS_FILENAME,
									application.readFromSdCard());
							refreshList();
							dialog.dismiss();
						}
					});
			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			alert.show();
			return false;
		case R.id.MENU_save_foods:
			// TODO Export to file
			AlertDialog.Builder alert2 = new AlertDialog.Builder(mContext);
			alert2.setTitle("Export to file");
			alert2.setMessage(getResources().getString(
					R.string.export_instructions));
			alert2.setCancelable(true);
			alert2.setIcon(getResources()
					.getDrawable(android.R.drawable.ic_menu_save));
			alert2.setPositiveButton("Export",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							application.writeToSdCard(myFoodsArrayList);
							Toast.makeText(mContext, "Exported!", Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						}
					});
			alert2.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			alert2.show();
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
