package com.simicart.core.notification;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.webkit.JsResult;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.simicart.R;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.network.error.SimiErrorListener;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataPreferences;
import com.simicart.core.splash.entity.StoreViewBaseEntity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by frank on 7/12/16.
 */
public class SCRegistrationIntentService extends IntentService {

    double longtitude;
    double lattitude;

    public SCRegistrationIntentService() {
        super("");
        getCurrentLocation();
    }

    protected void getCurrentLocation() {
        GPSTracker gps = new GPSTracker();
        longtitude = gps.getLongitude();
        lattitude = gps.getLatitude();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);

            String defaultSenderId = AppStoreConfig.getInstance().getSenderID();
            Log.e("SCRegistrationIntentService ", "=========> defaultSenderId " + defaultSenderId);

            String token = instanceID.getToken(defaultSenderId,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i("SCRegistrationIntentService", "GCM Registration Token: " + token);

            sendRegistrationToServer(token);

            // Subscribe to topic channels
            //subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(Constants.Notification.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d("SCRegistrationIntentService", "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(Constants.Notification.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
//        Intent registrationComplete = new Intent(Constants.Notification.REGISTRATION_COMPLETE);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(String token) {
        try {
            JSONObject jsParam = getDataForRegister(token);
            RegisterDeviceModel model = new RegisterDeviceModel();

            model.setDelegate(new ModelDelegate() {
                @Override
                public void onSuccess(SimiCollection collection) {
                    Log.e("SCRegistrationIntentService ", "------> REGISTER SUCCESS");
                }
            });

            model.setErrorListener(new SimiErrorListener() {
                @Override
                public void onErrorListener(SimiError error) {
                    Log.e("SCRegistrationIntentService ", "------> REGISTER FAIL");
                }
            });

            model.setJSONBOdy(jsParam);

            model.request();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private JSONObject getDataForRegister(String token) throws JSONException {
        JSONObject json = new JSONObject();

        json.put("device_token", token);
        json.put("is_demo", "1");
        json.put("latitude", lattitude);
        json.put("longitude", longtitude);
        String email = DataPreferences.getCustomerEmail();
        if (Utils.validateString(email)) {
            json.put("user_email", email);
        }
        json.put("plaform_id", "3");
        PackageManager manager = this.getPackageManager();
        try {
            String packageName = this.getPackageName();
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            json.put("build_version", info.versionCode);
            json.put("app_id", packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return json;

    }

}
