package com.simicart.core.banner.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;

public class BannerModel extends SimiModel {

	@Override
	protected void setUrlAction() {
		mUrlAction =  "connector/config/get_banner";
	}

	@Override
	protected void setShowNotifi() {
		this.isShowNotify = false;
	}

	@Override
	protected void setEnableCache() {
		this.enableCache = true;
	}
}
