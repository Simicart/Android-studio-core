package com.simicart.plugins.custompayment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.common.KeyEvent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by frank on 30/08/2016.
 */
public class CustomPayment {

    public CustomPayment() {

    }

    protected void requestCustomPayment() {
        CustomPaymentModel model = new CustomPaymentModel();
        model.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                ArrayList<SimiEntity> entities = collection.getCollection();
                if (null != entities && entities.size() > 0) {
                    for (int i = 0; i < entities.size(); i++) {
                        CustomPaymentEntity customPaymentEntity = new CustomPaymentEntity();
                        JSONObject json = entities.get(i).getJSONObject();
                        customPaymentEntity.parse(json);
                        registerEvent(customPaymentEntity);
                    }
                }
            }
        });
        model.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {

            }
        });

        model.request();
    }

    protected void registerEvent(final CustomPaymentEntity customPaymentEntity) {

        String methodName = customPaymentEntity.getPaymentMethod();
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra("data");
                SimiData data = bundle.getParcelable("entity");
                HashMap<String, Object> hm = data.getData();
                hm.put("custom_payment_entity", customPaymentEntity);
                SimiData simiData = new SimiData(hm);
                CustomPaymentFragment fragment = CustomPaymentFragment.newIntance(simiData);
                SimiManager.getIntance().replaceFragment(fragment);
            }
        };
        String nameEvent = KeyEvent.REVIEW_ORDER.FOR_PAYMENT_TYPE_WEBVIEW + methodName;
        SimiEvent.registerEvent(nameEvent, receiver);

    }

}
