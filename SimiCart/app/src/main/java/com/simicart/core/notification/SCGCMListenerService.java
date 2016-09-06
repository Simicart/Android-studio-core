package com.simicart.core.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.magestore.simicart.R;
import com.simicart.MainActivity;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.Utils;
import com.simicart.core.splashscreen.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by frank on 7/12/16.
 */
public class SCGCMListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String s, Bundle data) {
        String message = data.getString("message");

        Log.e("SCGCMListenerService", " MESSAGE NOTIFICATION" + message);
        showNotification(message);
    }

    private void showNotification(String message) {
        if (Utils.validateString(message)) {
            try {
                JSONObject json = new JSONObject(message);
                NotificationEntity notificationEntity = new NotificationEntity();
                notificationEntity.parse(json);
                if (MainActivity.isActive && notificationEntity.isShowPopup()) {
                    Intent intent = new Intent("com.simicart.localnotification");
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("notification_entity", notificationEntity);
                    SimiData simiData = new SimiData(data);
                    intent.putExtra("data", simiData);
                    LocalBroadcastManager.getInstance(this).sendBroadcastSync(intent);
                } else {
                    makeNotification(notificationEntity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void makeNotification(NotificationEntity notificationEntity) {
        Intent intent = new Intent(this, SplashActivity.class);
        if(notificationEntity.isShowPopup()){
            intent.putExtra("NOTIFICATION_DATA",notificationEntity);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.default_logo)
                .setContentTitle(notificationEntity.getTitle())
                .setContentText(notificationEntity.getmMessage())
                .setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS)
                .setSound(defaultSoundUri)
                .setOngoing(true)
                .setLights(0xff00ff00, 300, 100)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(Utils.generateViewId(), notificationBuilder.build());
    }


}
