package com.simicart.core.splashscreen.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

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
