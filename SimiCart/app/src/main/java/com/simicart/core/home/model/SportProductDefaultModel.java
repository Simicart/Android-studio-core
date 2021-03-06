package com.simicart.core.home.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;

public class SportProductDefaultModel extends SimiModel {

    @Override
    protected void setUrlAction() {
        mUrlAction = Constants.GET_SPOT_PRODUCTV2;
    }

    @Override
    protected void setEnableCache() {
        this.enableCache = true;
    }
}
