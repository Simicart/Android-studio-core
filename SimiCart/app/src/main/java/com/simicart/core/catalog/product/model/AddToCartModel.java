package com.simicart.core.catalog.product.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;

public class AddToCartModel extends SimiModel {

	@Override
	protected void parseData() {
		// TODO Auto-generated method stub
		super.parseData();
	}

	@Override
	protected void setUrlAction() {
		mUrlAction = Constants.ADD_TO_CART;
	}


}
