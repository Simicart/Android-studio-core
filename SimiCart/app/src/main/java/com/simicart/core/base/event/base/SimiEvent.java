package com.simicart.core.base.event.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;

import java.util.HashMap;

/**
 * Created by frank on 27/08/2016.
 */
public class SimiEvent {
    public static void dispatchEvent(String eventName, HashMap<String, Object> hmData) {
        Context context = SimiManager.getIntance().getCurrentActivity();
        Intent intent = new Intent(eventName);
        Bundle bundle = new Bundle();
        SimiData data = new SimiData(hmData);
        bundle.putParcelable("entity", data);
        intent.putExtra("data", bundle);
        LocalBroadcastManager.getInstance(context).sendBroadcastSync(intent);
    }

    public static void registerEvent(String eventName, BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter(eventName);
        Context context = SimiManager.getIntance().getCurrentActivity();
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);
    }

}
