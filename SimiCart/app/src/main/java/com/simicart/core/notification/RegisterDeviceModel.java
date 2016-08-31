package com.simicart.core.notification;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.network.request.SimiRequest;

/**
 * Created by frank on 7/12/16.
 */
public class RegisterDeviceModel extends SimiModel {

    @Override
    protected void setUrlAction() {
        addDataExtendURL("simiconnector/rest/v2/devices");
    }

    @Override
    protected void parseData() {
        super.parseData();
    }

    @Override
    protected void setTypeMethod() {
        mTypeMethod = SimiRequest.Method.POST;
    }
}
