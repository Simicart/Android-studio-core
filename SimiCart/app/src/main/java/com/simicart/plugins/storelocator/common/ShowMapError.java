package com.simicart.plugins.storelocator.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import com.simicart.core.base.translate.SimiTranslator;


public class ShowMapError {
	private Activity mActivity;

	public ShowMapError(Activity activity) {
		mActivity = activity;
	}

	public void showDiagloError(String title, String message) {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
		builder1.setTitle(title);
		builder1.setMessage(message);
		builder1.setCancelable(true);
		builder1.setPositiveButton(SimiTranslator.getInstance().translate("OK"),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}

	public void showGpsError() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
		builder1.setTitle(SimiTranslator.getInstance().translate(
				"Location services disabled"));
		builder1.setMessage(SimiTranslator.getInstance().translate("Store Locator")
				+ SimiTranslator.getInstance()
						.translate(
								" needs access to your location. Please turn on location access."));
		builder1.setCancelable(true);
		builder1.setNegativeButton(SimiTranslator.getInstance().translate("Ignore"),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder1.setPositiveButton(SimiTranslator.getInstance().translate("Setting"),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						mActivity.startActivity(intent);
					}
				});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}
}
