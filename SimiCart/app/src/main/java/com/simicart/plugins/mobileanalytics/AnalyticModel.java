package com.simicart.plugins.mobileanalytics;

import com.simicart.core.base.model.SimiModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by frank on 06/09/2016.
 */
public class AnalyticModel extends SimiModel {

    protected String mTrackerID;

    @Override
    protected void parseData() {
        try {
            JSONArray list = this.mJSON.getJSONArray("data");
            if (null != list && list.length() > 0) {
                JSONObject json = list.getJSONObject(0);
                mTrackerID = json.getString("ga_id");
            }
        } catch (JSONException e) {

        }
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = "manalytics/api/get_ga_id";
    }

    @Override
    protected void setShowNotifi() {
        isShowNotify = false;
    }

    public String getTrackerID() {
        return mTrackerID;
    }
}
