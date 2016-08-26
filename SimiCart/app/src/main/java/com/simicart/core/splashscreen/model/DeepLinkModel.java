package com.simicart.core.splashscreen.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;

public class DeepLinkModel extends SimiModel  {

    @Override
    protected void setShowNotifi() {
        isShowNotify = false;
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = Constants.DEEP_LINK;
    }
}
