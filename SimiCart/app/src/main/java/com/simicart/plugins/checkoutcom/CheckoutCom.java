package com.simicart.plugins.checkoutcom;

import com.simicart.core.base.payment.Payment;
import com.simicart.core.common.KeyEvent;

/**
 * Created by frank on 30/08/2016.
 */
public class CheckoutCom extends Payment {

    @Override
    protected String getKeyEvent() {
        return KeyEvent.REVIEW_ORDER.FOR_PAYMENT_TYPE_WEBVIEW + "SIMICHECKOUTCOM";
    }

    @Override
    protected void openPaymentPage() {
        super.openPaymentPage();
    }
}
