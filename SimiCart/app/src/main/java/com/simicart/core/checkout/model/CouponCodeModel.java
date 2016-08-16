package com.simicart.core.checkout.model;

import com.simicart.core.config.Constants;

public class CouponCodeModel extends ShippingMethodModel {

	@Override
	protected void setUrlAction() {
		mUrlAction = Constants.SET_COUPON;
	}

}
