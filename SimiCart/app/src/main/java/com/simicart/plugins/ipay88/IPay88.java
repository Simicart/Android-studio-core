package com.simicart.plugins.ipay88;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.payment.Payment;
import com.simicart.core.common.KeyEvent;
import com.simicart.plugins.paypal.PayPalActivity;

/**
 * Created by MSI on 30/08/2016.
 */
public class IPay88 extends Payment {

    @Override
    protected String getKeyEvent() {
        return KeyEvent.REVIEW_ORDER.FOR_PAYMENT_TYPE_WEBVIEW +"SIMIIPAY88";
    }

    @Override
    protected void openPaymentPage() {
        SimiData data = new SimiData(mData);
        Context context = SimiManager.getIntance().getCurrentActivity();
        Intent intent = new Intent(context,Ipay88Activity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }
}
