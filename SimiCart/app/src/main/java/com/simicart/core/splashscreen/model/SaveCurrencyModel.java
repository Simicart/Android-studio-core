package com.simicart.core.splashscreen.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;

public class SaveCurrencyModel extends SimiModel {

	@Override
	protected void parseData() {
		super.parseData();
	}

	@Override
	protected void setUrlAction() {
		mUrlAction = Constants.SAVE_CURRENCY;
	}

}
