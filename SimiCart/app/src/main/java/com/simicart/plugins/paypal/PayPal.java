package com.simicart.plugins.paypal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.payment.Payment;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;

/**
 * Created by frank on 30/08/2016.
 */
public class PayPal extends Payment {

    public PayPal() {
        super();
    }

    @Override
    protected String getKeyEvent() {
        Log.e("PayPal ", "getKeyEvent " + KeyEvent.REVIEW_ORDER.FOR_PAYMENT_TYPE_SDK + "PAYPAL_MOBILE");
        return KeyEvent.REVIEW_ORDER.FOR_PAYMENT_TYPE_SDK + "PAYPAL_MOBILE";
    }

    @Override
    protected void openPaymentPage() {
        Log.e("PayPal ", "openPaymentPage");
        SimiData data = new SimiData(mData);
        Context context = SimiManager.getIntance().getCurrentActivity();
        Intent intent = new Intent(context, PayPalActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        intent.putExtras( bundle);
        context.startActivity(intent);
    }
}
