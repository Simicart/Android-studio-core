package com.simicart.core.customer.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;

public class EditProfileModel extends SimiModel {


	@Override
	protected void setUrlAction() {
		mUrlAction = Constants.CHANGE_USER;
	}

}