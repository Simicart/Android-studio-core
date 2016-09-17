package com.simicart.core.splashscreen.model;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.AppCheckoutConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.customer.entity.ConfigCustomerAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreViewModel extends SimiModel {

    @Override
    protected void parseData() {
        try {
            JSONArray list = this.mJSON.getJSONArray("data");
            JSONObject jsonResult = list.getJSONObject(0);
            parseJSONStoreView(jsonResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseJSONStoreView(JSONObject jsonResult) throws JSONException {

        Log.e("StoreViewModel", "parseJSONStoreView " + jsonResult.toString());

        // parse store config
        JSONObject jsStoreConfig = jsonResult.getJSONObject(
                "store_config");
        AppStoreConfig.getInstance().parse(jsStoreConfig);

        // parse checkout config
        JSONObject jsCheckoutConfig = jsonResult.getJSONObject("checkout_config");
        AppCheckoutConfig.getInstance().parse(jsCheckoutConfig);

        // parse sender id
        if (jsonResult.has("android_sender")) {
            String sender = jsonResult.getString("android_sender");
            AppStoreConfig.getInstance().setSenderID(sender);
        }

        // parse type of product list (category detail: List or Grid)
        if (jsonResult.has("view_products_default")) {
            String type = jsonResult.getString("view_products_default");
            AppStoreConfig.getInstance().setTypeProductList(type);
        }

        if (jsonResult.has("customer_address_config")) {
            JSONObject jsAddress = jsonResult.getJSONObject("customer_address_config");
            ConfigCustomerAddress.getInstance().parse(jsAddress);
        }

    }


    @Override
    protected void setUrlAction() {
        mUrlAction = Constants.GET_STORE_VIEW;
    }


}
