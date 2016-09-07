package com.simicart.plugins.paypalexpress.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.common.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by frank on 9/7/16.
 */
public class StartExpressModel extends SimiModel {

    private String mUrl;
    private boolean isReviewAddress;

    @Override
    protected void setUrlAction() {
        mUrlAction = "paypalexpress/api/start";
    }


    @Override
    protected void parseData() {
        try {
            if (mJSON.has("data")) {
                JSONArray array = mJSON.getJSONArray("data");
                if (null != array && array.length() > 0) {
                    JSONObject json = array.getJSONObject(0);

                    if (json.has("url")) {
                        mUrl = json.getString("url");
                    }

                    if (json.has("review_address")) {
                        String reviewAddress = json.getString("review_address");
                        if (Utils.TRUE(reviewAddress)) {
                            setReviewAddress(true);
                        }
                    }
                }
            }

        } catch (Exception e) {

        }
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public boolean isReviewAddress() {
        return isReviewAddress;
    }

    public void setReviewAddress(boolean reviewAddress) {
        isReviewAddress = reviewAddress;
    }
}
