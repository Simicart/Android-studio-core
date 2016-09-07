package com.simicart.core.splashscreen.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;

public class StoreModel extends SimiModel {

    @Override
    protected void parseData() {
        // TODO Auto-generated method stub
        super.parseData();
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = Constants.GET_STORES;
    }
}
