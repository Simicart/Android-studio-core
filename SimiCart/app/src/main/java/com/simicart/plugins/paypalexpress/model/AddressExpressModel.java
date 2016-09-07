package com.simicart.plugins.paypalexpress.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.customer.entity.AddressEntity;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by frank on 9/7/16.
 */
public class AddressExpressModel extends SimiModel {

    protected AddressEntity mShippingAddress;
    protected AddressEntity mBillingAddress;

    @Override
    protected void parseData() {
        try {
            if (mJSON.has("data")) {
                JSONArray array = mJSON.getJSONArray("data");
                if (null != array && array.length() > 0) {
                    JSONObject json = array.getJSONObject(0);
                    if (json.has("shipping_address")) {
                        JSONObject jsShippingAddress = json.getJSONObject("shipping_address");
                        mShippingAddress = new AddressEntity();
                        mShippingAddress.parse(jsShippingAddress);
                    }

                    if (json.has("billing_address")) {
                        JSONObject jsBillingAddress = json.getJSONObject("billing_address");
                        mBillingAddress = new AddressEntity();
                        mBillingAddress.parse(jsBillingAddress);
                    }

                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = "paypalexpress/api/review_address";
    }

    public AddressEntity getShippingAddress() {
        return mShippingAddress;
    }

    public void setShippingAddress(AddressEntity mShippingAddress) {
        this.mShippingAddress = mShippingAddress;
    }

    public AddressEntity getBillingAddress() {
        return mBillingAddress;
    }

    public void setBillingAddress(AddressEntity mBillingAddress) {
        this.mBillingAddress = mBillingAddress;
    }
}
