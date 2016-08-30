package com.simicart.plugins.paypal;

import com.simicart.core.base.model.SimiModel;

/**
 * Created by MSI on 30/08/2016.
 */
public class PaypalModel extends SimiModel {
    @Override
    protected void setUrlAction() {
        mUrlAction = "paypalmobile/index/update_paypal_payment";
    }
}
