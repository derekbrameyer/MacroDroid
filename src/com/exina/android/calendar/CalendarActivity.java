/*
 * Copyright (C) 2011 Chris Gao <chris@exina.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exina.android.calendar;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.doomonafireball.macrodroid.R;

public class CalendarActivity extends Activity implements
		CalendarView.OnCellTouchListener {
	public static final String MIME_TYPE = "vnd.android.cursor.dir/vnd.exina.android.calendar.date";
	CalendarView mView = null;
	TextView mHit;
	TextView monthYearTV;
	Button prevMonthBTN;
	Button nextMonthBTN;
	SimpleDateFormat monthYearFormat;
	Handler mHandler = new Handler();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mView = (CalendarView) findViewById(R.id.calendar);
		mView.setOnCellTouchListener(this);
		monthYearTV = (TextView) findViewById(R.id.TV_month_year);
		prevMonthBTN = (Button) findViewById(R.id.BTN_prev_month);
		nextMonthBTN = (Button) findViewById(R.id.BTN_next_month);
		monthYearFormat = new SimpleDateFormat("MMMMM yyyy");
		prevMonthBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mView.previousMonth();
				setMonthYearText();
			}			
		});
		nextMonthBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mView.nextMonth();
				setMonthYearText();
			}			
		});
		setMonthYearText();

		/*if (getIntent().getAction().equals(Intent.ACTION_PICK))
			findViewById(R.id.hint).setVisibility(View.INVISIBLE);*/
	}
	
	private void setMonthYearText() {
		Date d = new Date();
		d.setMonth(mView.getMonth());
		d.setYear(mView.getYear() - 1900);
		monthYearTV.setText(monthYearFormat.format(d));
	}

	public void onTouch(Cell cell) {
		Intent intent = getIntent();
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_PICK)
				|| action.equals(Intent.ACTION_GET_CONTENT)) {
			Intent ret = new Intent();
			ret.putExtra("year", mView.getYear());
			ret.putExtra("month", mView.getMonth());
			ret.putExtra("day", cell.getDayOfMonth());
			this.setResult(RESULT_OK, ret);
			finish();
			return;
		}
		int day = cell.getDayOfMonth();
		if (mView.firstDay(day))
			mView.previousMonth();
		else if (mView.lastDay(day))
			mView.nextMonth();
		else
			return;

		mHandler.post(new Runnable() {
			public void run() {
				Toast.makeText(
						CalendarActivity.this,
						DateUtils.getMonthString(mView.getMonth(),
								DateUtils.LENGTH_LONG) + " " + mView.getYear(),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}