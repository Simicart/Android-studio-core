package com.simicart.plugins.ccaveneu;

import com.simicart.core.base.payment.Payment;
import com.simicart.core.common.KeyEvent;

/**
 * Created by frank on 8/28/16.
 */
public class CCAveneu extends Payment {

    public CCAveneu() {
        super();
    }

    @Override
    protected void openPaymentPage() {

    }

    @Override
    protected String getKeyEvent() {
        return KeyEvent.REVIEW_ORDER.FOR_PAYMENT_TYPE_WEBVIEW + "SIMIAVENUE";
    }
}
