package com.simicart.core.splashscreen.model;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.AppColorConfig;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by frank on 17/08/2016.
 */
public class AppConfigModel extends SimiModel {


    @Override
    protected void parseData() {
        Log.e("AppConfigModel ","DATA " + mJSON.toString());
        collection = new SimiCollection();
        onParse();
    }

    protected void onParse() {
        try {
            if (mJSON.has("app-configs")) {
                JSONArray array = mJSON.getJSONArray("app-configs");
                if (null != array && array.length() > 0) {
                    JSONObject js_app = array.getJSONObject(0);
                    if(js_app.has("theme")) {
                        JSONObject themeObj = js_app.getJSONObject("theme");
                        AppColorConfig.getInstance().parse(themeObj);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void setCloud() {
        isCloud = true;
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = "/app-configs/";
    }
}
