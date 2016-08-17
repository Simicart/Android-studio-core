package com.simicart.core.config;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.simicart.core.common.Utils;

@SuppressLint("DefaultLocale")
public class Config {

    private String mBaseUrl = "http://www.kalkifashion.com/index.php";
    private String mSecretKey = "b60692583049b01d87d498530f9839e8";
    private String mBaseCloudUrl = "https://api.jajahub.com/rest/";
    private String mSecretCloudKey = "f77132fc93d049cc5e0fd3229c45ac786704ab61";

    private String mDemoEnable = "DEMO_ENABLE11";
    private String mColorSplashScreen = "#FFFFFF";
    private String mFontCustom = "fonts/ProximaNovaLight.ttf";
    private String mUseStore;
    private String isFullSplash;

    private String mCookie = "";

    private static Config instance;

    public static Config getInstance() {
        if (null == instance) {
            instance = new Config();
        }

        return instance;
    }

    public boolean isUseStore() {
        if (!Utils.validateString(mUseStore)) {
            return false;
        }

        if (mUseStore.equals("1")) {
            return true;
        }

        return false;
    }

    public boolean isFullSplash() {
        if (!Utils.validateString(isFullSplash)) {
            return false;
        }

        if (isFullSplash.equals("1")) {
            return true;
        }

        return false;
    }

    public boolean isDemoVersion() {
        if (!Utils.validateString(mDemoEnable)) {
            return false;
        }

        mDemoEnable = mDemoEnable.toUpperCase();
        if (mDemoEnable.equals("DEMO_ENABLE") || mDemoEnable.equals("YES")) {
            return true;
        }
        return false;
    }


    public void setDemoEnable(String demo_enable) {
        mDemoEnable = demo_enable;
    }

    public String getSecretKey() {
        return mSecretKey;
    }

    public String getSecretCloudKey() {
        return mSecretCloudKey;
    }

    public void setSecretKey(String secret_key) {
        mSecretKey = secret_key;
    }

    public String getConnectorUrl() {
        return mBaseUrl + "connector/";
    }

    public void setBaseUrl() {
        int length = mBaseUrl.length();
        char last = mBaseUrl.charAt(length - 1);
        if (last != '/') {
            mBaseUrl += "/";
        }

        int last_index = mBaseCloudUrl.length() - 1;
        char lastChar = mBaseCloudUrl.charAt(last_index);
        if (lastChar == '/') {
            mBaseCloudUrl = mBaseCloudUrl.substring(0, last_index);
        }
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public String getBaseCloudUrl() {
        return mBaseCloudUrl;
    }

    public void setBase_url(String base_url) {
        mBaseUrl = base_url;
    }

    public int getColorSplash() {
        return Color.parseColor(mColorSplashScreen);
    }

    public String getFontCustom() {
        return mFontCustom;
    }

    public void setFontCustom(String mFontCustom) {
        this.mFontCustom = mFontCustom;
    }

    public String getCookie() {
        return mCookie;
    }

    public void setCookie(String _cookie) {
        this.mCookie = _cookie;
    }

}
