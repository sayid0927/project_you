package com.zxly.o2o.dialog;

import android.app.DatePickerDialog;
import android.content.Context;

public class SettingDatePickerDialog extends DatePickerDialog {

	public SettingDatePickerDialog(Context context, OnDateSetListener callBack,
								   int year, int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
	}

	@Override
	protected void onStop() {
		// super.onStop();
	}
}
