package com.simicart.core.notification.model;

import com.simicart.core.base.model.SimiModel;

public class RegisterIDModel extends SimiModel {

	@Override
	protected void setShowNotifi() {
		isShowNotify = false;
	}

	@Override
	protected void parseData() {
		super.parseData();
	}

	@Override
	protected void setUrlAction() {
		mUrlAction = "connector/config/register_device";
	}
}
