package com.simicart.plugins.hideaddress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.common.KeyEvent;

/**
 * Created by Glenn on 9/6/2016.
 */
public class HiddenAddress {

    public HiddenAddress() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                HiddenAddressModel hiddenAddressModel = new HiddenAddressModel();
                hiddenAddressModel.setSuccessListener(new ModelSuccessCallBack() {
                    @Override
                    public void onSuccess(SimiCollection collection) {

                    }
                });
                hiddenAddressModel.request();
            }
        };
        SimiEvent.registerEvent(KeyEvent.HIDDEN_ADDRESS.HIDDEN_ADDRESS_GET_CONFIG_ADDRESS, receiver);
    }

}
