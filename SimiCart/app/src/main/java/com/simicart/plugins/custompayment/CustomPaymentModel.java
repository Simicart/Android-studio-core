package com.simicart.plugins.custompayment;

import com.simicart.core.base.model.SimiModel;

/**
 * Created by frank on 30/08/2016.
 */
public class CustomPaymentModel extends SimiModel {

    @Override
    protected void setUrlAction() {
        mUrlAction = "simicustompayment/api/get_custom_payments";
    }

    @Override
    protected void setShowNotifi() {
        isShowNotify = false;
    }
}
