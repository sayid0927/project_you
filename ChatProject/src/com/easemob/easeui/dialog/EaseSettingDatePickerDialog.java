package com.easemob.easeui.dialog;

import android.app.DatePickerDialog;
import android.content.Context;

public class EaseSettingDatePickerDialog extends DatePickerDialog {

	public EaseSettingDatePickerDialog(Context context, OnDateSetListener callBack,
									   int year, int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
	}

	@Override
	protected void onStop() {
		// super.onStop();
	}
}
