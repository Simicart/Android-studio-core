package com.simicart.plugins.ccaveneu;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
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
        SimiData data = new SimiData(mData);
        CCAveneuFragment fragment = CCAveneuFragment.newInstance(data);
        SimiManager.getIntance().replaceFragment(fragment);
    }

    @Override
    protected String getKeyEvent() {
        return KeyEvent.REVIEW_ORDER.FOR_PAYMENT_TYPE_WEBVIEW + "SIMIAVENUE";
    }
}
