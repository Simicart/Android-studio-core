package com.simicart.core.notification;

import com.simicart.core.base.model.SimiModel;

/**
 * Created by frank on 7/12/16.
 */
public class RegisterDeviceModel extends SimiModel {

    @Override
    protected void setUrlAction() {
        mUrlAction = "connector/config/register_device";
    }

    @Override
    protected void setShowNotifi() {
        isShowNotify = false;
    }
}
