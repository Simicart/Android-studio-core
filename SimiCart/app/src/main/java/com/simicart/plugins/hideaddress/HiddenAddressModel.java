package com.simicart.plugins.hideaddress;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Constants;
import com.simicart.core.customer.entity.ConfigCustomerAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Glenn on 9/6/2016.
 */
public class HiddenAddressModel extends SimiModel {

    @Override
    protected void parseData() {

        try {
            JSONArray list = this.mJSON.getJSONArray(Constants.DATA);
            collection = new SimiCollection();
            if (null != list && list.length() > 0) {
                JSONObject object = list.getJSONObject(0);
                ConfigCustomerAddress.getInstance().parse(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void setShowNotifi() {
        isShowNotify = false;
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = "hideaddress/api/get_address_show";
    }

}
