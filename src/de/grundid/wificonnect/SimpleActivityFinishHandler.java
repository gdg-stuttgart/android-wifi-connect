package de.grundid.wificonnect;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

public class SimpleActivityFinishHandler implements OnClickListener, OnCancelListener,
		android.view.View.OnClickListener {

	private Activity activityToFinish;

	public SimpleActivityFinishHandler(Activity activityToFinish) {
		this.activityToFinish = activityToFinish;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		activityToFinish.finish();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		activityToFinish.finish();
	}

	@Override
	public void onClick(View v) {
		activityToFinish.finish();
	}
}
