package com.simicart.core.notification.common;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.simicart.MainActivity;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.common.Utils;
import com.simicart.core.notification.gcm.GCMRegistrar;
import com.simicart.core.notification.model.RegisterIDModel;

/**
 * Helper class used to communicate with the demo server.
 */
public final class ServerUtilities {

	// /**
	// * Register this account/device pair within the server.
	// *
	// * @return whether the registration succeeded or not.
	// */
	public static void register(final Context context, final String regId,
			String latitude, String longitude) {
		RegisterIDModel model = new RegisterIDModel();
		model.setSuccessListener(new ModelSuccessCallBack() {
			@Override
			public void onSuccess(SimiCollection collection) {
				stopRegisterService();
//					Log.e(getClass().getName(), "RegisterIDModel: " + message);
					GCMRegistrar.setRegisteredOnServer(context, true);
					GCMRegistrar.setRegistrationId(context, regId);
			}
		});
		model.setFailListener(new ModelFailCallBack() {
			@Override
			public void onFail(SimiError error) {
				stopRegisterService();
			}
		});
		model.addBody("device_token", regId);
		if (Utils.validateString(longitude) && Utils.validateString(latitude)) {
			model.addBody("latitude", latitude);
			model.addBody("longitude", longitude);
		}
		model.request();
	}

	public static void startRegisterService(final Context context,
			final String regId, String latitude, String longitude) {
		Intent intent = new Intent(context, ServiceNotification.class);
		intent.putExtra("REGID", regId);
		intent.putExtra("LATI", latitude);
		intent.putExtra("LONG", longitude);
		context.startService(intent);
	}

	public static void stopRegisterService() {
		Context context = SimiManager.getIntance().getCurrentActivity();
		if (context != null) {
			context.stopService(new Intent(context, ServiceNotification.class));
		}
	}

	public static void unregister(Context context, String regId) {
		// TODO Auto-generated method stub
	}

}
