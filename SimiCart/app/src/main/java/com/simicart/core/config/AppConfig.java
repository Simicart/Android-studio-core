package com.simicart.core.config;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Martial on 8/15/2016.
 */
public class AppConfig extends SimiEntity {

    protected String mLayout;
    protected String mLocale;

    private String layout = "layout";
    private String theme = "theme";
    private String language = "language";

    @Override
    public void parse() {
        if (mJSON != null) {

            if (hasKey(layout)) {
                mLayout = getData(layout);
            }

            if (hasKey(theme)) {
                String themeValue = getData(theme);

                if (Utils.validateString(themeValue)) {
                    try {
                        AppColorConfig.getInstance().setJSONObject(new JSONObject(themeValue));
                        AppColorConfig.getInstance().parse();
                    } catch (JSONException e) {
                    }
                }
            }

            if (hasKey(language)) {
                try {
                    JSONObject languageObj = mJSON.getJSONObject(language);
                    Iterator keys = languageObj.keys();
                    while (keys.hasNext()) {
                        mLocale = (String) keys.next();
                    }
                } catch (JSONException e) {
                    mLocale = null;
                }
            }
        }
    }


    public String getLayout() {
        return mLayout;
    }

    public void setLayout(String mLayout) {
        this.mLayout = mLayout;
    }


    public String getLocale() {
        return mLocale;
    }

    public void setLocale(String mLocale) {
        this.mLocale = mLocale;
    }

}
