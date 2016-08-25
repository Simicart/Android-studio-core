package com.simicart.core.checkout.model;

import com.simicart.core.config.Constants;

public class ShippingMethodModel extends PlaceOrderModel {


	@Override
	protected void setUrlAction() {
		mUrlAction = Constants.SAVE_SHIPPING_METHOD;
	}

}
