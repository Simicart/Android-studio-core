package com.simicart.core.checkout.model;

import com.simicart.core.config.Constants;

public class CouponCodeModel extends ReviewOrderModel {

    @Override
    protected void setUrlAction() {
        mUrlAction = Constants.SET_COUPON;
    }

}
