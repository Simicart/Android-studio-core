package com.simicart.plugins.braintree.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;

import org.json.JSONArray;
import org.json.JSONException;

public class BrainTreeModel extends SimiModel {

    protected String message;

    @Override
    protected void parseData() {
        try {
            if (mJSON.has("message")) {
                JSONArray messArr = mJSON.getJSONArray("message");
                if (messArr.length() > 0) {
                    message = messArr.getString(0);
                }
            }
            JSONArray js_data = this.mJSON.getJSONArray("data");
            if (null == collection) {
                collection = new SimiCollection();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = "simibraintree/index/update_payment";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
