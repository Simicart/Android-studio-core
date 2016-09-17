package com.simicart.core.checkout.model;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.checkout.entity.OrderInforEntity;
import com.simicart.core.config.Constants;

import org.json.JSONArray;
import org.json.JSONException;

public class PlaceOrderModel extends SimiModel {


    @Override
    protected void parseData() {
        try {
            Log.e("PlaceOrderModel JSON : ", mJSON.toString());

            JSONArray array = this.mJSON.getJSONArray("data");
            if (null != array && array.length() > 0) {
                collection = new SimiCollection();
                OrderInforEntity orderInforEntity = new OrderInforEntity();
                orderInforEntity.parse(array.getJSONObject(0));
                collection.addEntity(orderInforEntity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = Constants.PLACE_ORDER;
    }

}
