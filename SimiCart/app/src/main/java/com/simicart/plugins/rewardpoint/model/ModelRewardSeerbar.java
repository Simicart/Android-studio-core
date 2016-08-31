package com.simicart.plugins.rewardpoint.model;

import com.simicart.core.checkout.model.CouponCodeModel;
import com.simicart.core.checkout.model.ReviewOrderModel;
import com.simicart.plugins.rewardpoint.utils.Constant;

public class ModelRewardSeerbar extends ReviewOrderModel {

	@Override
	protected void setUrlAction() {
		mUrlAction = Constant.CHANGE_SEERBAR;
	}

}
