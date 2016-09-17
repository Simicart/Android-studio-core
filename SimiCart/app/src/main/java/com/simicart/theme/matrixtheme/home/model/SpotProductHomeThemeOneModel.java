package com.simicart.theme.matrixtheme.home.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.theme.matrixtheme.home.entity.OrderProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SpotProductHomeThemeOneModel extends SimiModel {

    @Override
    protected void parseData() {
        try {
            JSONArray list = this.mJSON.getJSONArray("data");

            if (null == collection) {
                collection = new SimiCollection();
            }
            for (int i = 0; i < list.length(); i++) {
                JSONObject data = list.getJSONObject(i);
                OrderProduct spotProduct = new OrderProduct(data);
                collection.addEntity(spotProduct);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = "themeone/api/get_order_spots";
    }

    @Override
    protected void setEnableCache() {
        this.enableCache = true;
    }
}