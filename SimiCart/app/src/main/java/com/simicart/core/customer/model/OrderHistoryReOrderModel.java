package com.simicart.core.customer.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;

public class OrderHistoryReOrderModel extends SimiModel {

    @Override
    protected void setUrlAction() {
        mUrlAction = Constants.ORDER_DETAIL_REORDER;
    }

    @Override
    protected void setEnableCache() {
        enableCache = true;
    }
}
