package com.simicart.core.customer.model;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddressBookDetailModel extends SimiModel {
    protected String mCountry;

    @Override
    protected void parseData() {
        try {
            JSONArray list = this.mJSON.getJSONArray("data");
            collection = new SimiCollection();
            if (null != list && list.length() > 0) {
                JSONObject obj = list.getJSONObject(0);
                Log.e("AddressBookDetailModel ", "JSON SAVE : " + obj.toString());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = Constants.SAVE_ADDRESS;
    }

    public void setTextCountry(String country) {
        mCountry = country;
    }
}
