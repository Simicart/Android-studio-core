package com.simicart.theme.matrixtheme.home.entity;

import android.util.Log;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderProduct extends SimiEntity {

    private ArrayList<String> mUrlImage;
    private String mSpotId;
    private String mSpotKey;
    private String mSpotName;


    public OrderProduct(JSONObject json) {
        parse(json);
    }

    public void parse(JSONObject json) {
        mJSON = json;
        try {
            if (json.has(Constants.SPOT_ID)) {
                mSpotId = json.getString(Constants.SPOT_ID);
            }
            if (json.has(Constants.SPOT_KEY)) {
                mSpotKey = json.getString(Constants.SPOT_KEY);
            }
            if (json.has(Constants.SPOT_NAME)) {
                mSpotName = json.getString(Constants.SPOT_NAME);
            }
            if (json.has(Constants.IMAGES)) {

                JSONArray array = getJSONArrayWithKey(json, "images");
                if (null != array && array.length() > 0) {
                    Log.e("OrderProduct", "ARRAY IMAGE " + array.toString());
                    mUrlImage = new ArrayList<>();
                    parseListImage(array);
                }

            }
        } catch (Exception e) {

        }

    }

    protected void parseListImage(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            String image = array.getString(i);
            Log.e("OrderProduct", "IMAGES " + image);
            mUrlImage.add(image);
        }
    }

    public ArrayList<String> getUrlImage() {
        return mUrlImage;
    }

    public void setUrlImage(ArrayList<String> mUrlImage) {
        this.mUrlImage = mUrlImage;
    }

    public String getSpotId() {
        return mSpotId;
    }

    public void setSpotId(String mSpotId) {
        this.mSpotId = mSpotId;
    }

    public String getSpotKey() {
        return mSpotKey;
    }

    public void setSpotKey(String mSpotKey) {
        this.mSpotKey = mSpotKey;
    }

    public String getSpotName() {
        return mSpotName;
    }

    public void setSpotName(String mSpotName) {
        this.mSpotName = mSpotName;
    }

}
