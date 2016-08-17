package com.simicart.core.customer.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;

public class SignOutModel extends SimiModel {


	@Override
	protected void setUrlAction() {
		mUrlAction = Constants.SIGN_OUT;
	}

}
