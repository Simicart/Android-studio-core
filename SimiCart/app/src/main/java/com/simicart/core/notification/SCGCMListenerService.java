package com.simicart.core.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.simicart.R;
import com.simicart.core.MainActivity;
import com.simicart.core.common.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by frank on 7/12/16.
 */
public class SCGCMListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String s, Bundle data) {
        String message = data.getString("message");
        Log.e("SCGCMListenerService","++++++++ MESSAGE " + message);
        sendNotification(message);
    }

    private void showNotification(String message){
        if(Utils.validateString(message)){
            try {
                JSONObject json = new JSONObject(message);
                NotificationEntity notificationEntity =new NotificationEntity();
                notificationEntity.parse(json);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.default_logo)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
