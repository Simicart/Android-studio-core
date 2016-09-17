package com.simicart.core.splashscreen.model;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.network.request.SimiRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by frank on 17/08/2016.
 */
public class ListSKUPluginModel extends SimiModel {
    private ArrayList<String> mSKU;
    private String public_plugins = "public_plugins";
    private String sku = "sku";

    public ArrayList<String> getListSKU() {
        return mSKU;
    }

    @Override
    protected void setCloud() {
        isCloud = true;
    }


    @Override
    protected void setUrlAction() {
        mUrlAction = "/public-plugins";

    }

    @Override
    protected void setTypeMethod() {
        mTypeMethod = SimiRequest.Method.GET;
    }

    @Override
    protected void parseData() {
        Log.e("ListSKUPluginModel ", "DATA " + mJSON.toString());
        if (mJSON.has(public_plugins)) {
            try {
                mSKU = new ArrayList<>();
                JSONArray array = mJSON.getJSONArray(public_plugins);
                if (null != array && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonPlugin = array.getJSONObject(i);
                        if (jsonPlugin.has(sku)) {
                            String sku_plugin = jsonPlugin.getString(sku);
                            mSKU.add(sku_plugin);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
