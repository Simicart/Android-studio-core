package com.simicart.core.checkout.model;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.checkout.entity.ReviewOrderEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReviewOrderModel extends SimiModel {


    @Override
    protected void parseData() {
        try {
            JSONArray array = this.mJSON.getJSONArray("data");
            if (null != array && array.length() > 0) {
                collection = new SimiCollection();
                JSONObject json = array.getJSONObject(0);
                ReviewOrderEntity reviewOrderEntity = new ReviewOrderEntity();
                reviewOrderEntity.parse(json);
                collection.addEntity(reviewOrderEntity);
            }
        } catch (JSONException e) {
            Log.e("ReviewOrderModel ","parse DATA " + e.getMessage());
        }
    }


    @Override
    protected void setUrlAction() {
        mUrlAction = "connector/checkout/get_order_config";
    }


}
