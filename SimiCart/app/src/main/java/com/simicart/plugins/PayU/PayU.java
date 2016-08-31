package com.simicart.plugins.PayU;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.payment.Payment;
import com.simicart.core.common.KeyEvent;

/**
 * Created by MSI on 30/08/2016.
 */
public class PayU extends Payment {

    @Override
    protected String getKeyEvent() {
        return KeyEvent.REVIEW_ORDER.FOR_PAYMENT_TYPE_WEBVIEW + "SIMIPAYU";
    }

    @Override
    protected void openPaymentPage() {
        SimiData data = new SimiData(mData);
        PayUFragment fragment = PayUFragment.newInstance(data);
        SimiManager.getIntance().replaceFragment(fragment);
    }
}
