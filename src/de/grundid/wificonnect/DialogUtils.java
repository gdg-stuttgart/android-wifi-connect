package de.grundid.wificonnect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class DialogUtils {

	public static void buildAndShowInformationDialog(Context context, int infoMessageId,
			OnClickListener positiveOnClickListener) {
		buildInformationDialog(context, infoMessageId, positiveOnClickListener).create().show();
	}

	public static AlertDialog.Builder buildInformationDialog(Context context, int infoMessageId,
			OnClickListener positiveOnClickListener) {
		return new AlertDialog.Builder(context).setIcon(android.R.drawable.ic_dialog_info).setMessage(infoMessageId)
				.setNeutralButton(android.R.string.ok, positiveOnClickListener);
	}
}
