package com.simicart.core.notification.common;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.simicart.MainActivity;
import com.simicart.core.base.manager.SimiManager;

public class ServiceNotification extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        // Toast.makeText(this, "Service onBind", Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        String regid = intent.getStringExtra("REGID");
        String longitude = intent.getStringExtra("LONG");
        String latitude = intent.getStringExtra("LATI");
        Context context = SimiManager.getIntance().getCurrentActivity();
        ServerUtilities.register(context, regid,
                latitude, longitude);
        // Toast.makeText(this, "Service Started " + regid, Toast.LENGTH_LONG)
        // .show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

}
