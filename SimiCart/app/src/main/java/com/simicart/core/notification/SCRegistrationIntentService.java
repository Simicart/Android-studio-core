package com.simicart.core.notification;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.renderscript.Double2;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by frank on 7/12/16.
 */
public class SCRegistrationIntentService extends IntentService {


    protected final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    protected final String REGISTRATION_COMPLETE = "registrationComplete";

    double longtitude;
    double lattitude;

    public SCRegistrationIntentService() {
        super("");
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

            String token = instanceID.getToken(defaultSenderId,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]

            sendRegistrationToServer(token);

            // Subscribe to topic channels
            //subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
//        Intent registrationComplete = new Intent(Constants.Notification.REGISTRATION_COMPLETE);
//        LocalBroadcastManager.newInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(String token) {
        try {
            JSONObject jsParam = getDataForRegister(token);
            RegisterDeviceModel model = new RegisterDeviceModel();

            model.setFailListener(new ModelFailCallBack() {
                @Override
                public void onFail(SimiError error) {

                }
            });

            model.setSuccessListener(new ModelSuccessCallBack() {
                @Override
                public void onSuccess(SimiCollection collection) {

                }
            });

            model.setJSONBodyEntity(jsParam);

            model.request();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private JSONObject getDataForRegister(String token) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("device_token", token);
        if (Config.getInstance().isDemoVersion()) {
            json.put("is_demo", "1");
        } else {
            json.put("is_demo", "0");
        }

        if (Utils.validateString(DataLocal.latitude)) {
            try {
                lattitude = Double.parseDouble(DataLocal.latitude);
                json.put("latitude", lattitude);
            } catch (Exception e) {
                Log.e("Registeration Service ", " ------> Parse Lattitude " + e.getMessage());
            }
        }

        if (Utils.validateString(DataLocal.longtitude)) {
            try {
                longtitude = Double.parseDouble(DataLocal.longtitude);
                json.put("longitude", longtitude);

            } catch (Exception e) {
                Log.e("Registeration Service ", " ------> Parse Longtitude " + e.getMessage());
            }

        }
        return json;

    }

}
