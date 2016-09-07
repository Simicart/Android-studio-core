package com.simicart.plugins.klarna;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.KeyEvent;
import com.simicart.plugins.klarna.fragment.KlarnaFragment;

public class Klarna {

    public Klarna() {

        BroadcastReceiver klarnaReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SimiEvent.isRegistered = true;
                onCheckOut();
            }
        };

        String nameEvent = KeyEvent.REVIEW_ORDER.FOR_PAYMENT_BEFORE_PLACE + "SIMIKLARNA";
        SimiEvent.registerEvent(nameEvent, klarnaReceiver);

    }


    protected void onCheckOut() {
        KlarnaFragment fragment = new KlarnaFragment();
        SimiManager.getIntance().replacePopupFragment(fragment);
    }

}
