package com.simicart.core.base.payment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.Utils;

import java.util.HashMap;

/**
 * Created by frank on 8/28/16.
 */
public class Payment {

    protected HashMap<String, Object> mData;

    public Payment() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getDataFromIntent(intent);
                openPaymentPage();
            }
        };
        String keyEvent = getKeyEvent();
        if (Utils.validateString(keyEvent)) {
            SimiEvent.registerEvent(keyEvent, receiver);
        }
    }

    protected void getDataFromIntent(Intent intent) {
        Bundle bundle = intent.getBundleExtra("data");
        SimiData data = bundle.getParcelable("entity");
        mData = data.getData();
    }

    protected String getKeyEvent() {
        return null;
    }

    protected void openPaymentPage() {

    }
}
