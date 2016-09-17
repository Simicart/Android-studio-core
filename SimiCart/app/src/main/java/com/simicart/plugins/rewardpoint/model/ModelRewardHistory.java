package com.simicart.plugins.rewardpoint.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

public class ModelRewardHistory extends SimiModel {

    protected int mQty;

    public int getQty() {
        return mQty;
    }

    @Override
    protected void parseData() {
        try {
            JSONObject object = this.mJSON;
            collection = new SimiCollection();
            JSONObject objectData = object.getJSONObject("data");
            // Passbook passbook = new Passbook();
            // passbook.setJSONObject(objectData);
            // collection.addEntity(passbook);
            collection.setJSON(objectData);
            Collections.reverse(collection.getCollection());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void setUrlAction() {
        mUrlAction = "loyalty/point/history/";
    }

}
