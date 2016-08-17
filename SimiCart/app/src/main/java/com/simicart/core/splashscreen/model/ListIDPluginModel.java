package com.simicart.core.splashscreen.model;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.network.request.SimiRequest;
import com.simicart.core.common.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by frank on 17/08/2016.
 */
public class ListIDPluginModel extends SimiModel {

    private StringBuilder mIDBuilder = new StringBuilder();
    private String site_plugins = "site_plugins";
    private String config = "config";
    private String plugin_id = "plugin_id";
    private String enable = "enable";

    public String getListIDPlugin() {
        return mIDBuilder.toString();
    }
    @Override
    protected void setTypeMethod() {
        mTypeMethod = SimiRequest.Method.GET;
    }

    @Override
    protected void setCloud() {
        isCloud = true;
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = "/site-plugins";
    }

    @Override
    protected void parseData() {
        if (mJSON.has(site_plugins)) {
            try {
                Log.e("ListIDPluginModel ","DATA " + mJSON);
                JSONArray array = mJSON.getJSONArray(site_plugins);
                if (null != array && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonPlugins = array.getJSONObject(i);
                        if (jsonPlugins.has(config)) {
                            JSONObject jsonConfig = jsonPlugins.getJSONObject(config);
                            if (jsonConfig.has(enable)) {
                                String isEnable = jsonConfig.getString(enable);
                                if (Utils.validateString(isEnable) && isEnable.equals("1")) {
                                    String pluginID = jsonPlugins.getString(plugin_id);
                                    mIDBuilder.append(pluginID);
                                    mIDBuilder.append(",");
                                }
                            }

                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (mIDBuilder.length() > 0) {
            int lastIndex = mIDBuilder.length() - 1;
            mIDBuilder.deleteCharAt(lastIndex);
        }

    }
}
