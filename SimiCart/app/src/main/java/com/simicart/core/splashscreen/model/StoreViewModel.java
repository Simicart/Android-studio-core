package com.simicart.core.splashscreen.model;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppCheckoutConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.GenderConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StoreViewModel extends SimiModel {

    ConfigCustomerAddress configCustomerAddress = new ConfigCustomerAddress();

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

    }


    @Override
    protected void setUrlAction() {
        mUrlAction = Constants.GET_STORE_VIEW;
    }


}
