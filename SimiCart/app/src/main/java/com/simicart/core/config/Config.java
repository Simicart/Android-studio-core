package com.simicart.core.config;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.simicart.core.common.Utils;

@SuppressLint("DefaultLocale")
public class Config {

//    private String mBaseUrl = "BASE_URL";
//    private String mSecretKey = "SECRET_KEY";
//    private String mBaseCloudUrl = "BASE_CLOUD_URL";
//    private String mSecretCloudKey = "SECRET_CLOUD_KEY";
//    private String mColorSplashScreen = "SPLASH_SCREEN";
//    private String mDemoEnable = "DEMO_ENABLE";

    private String mBaseUrl = "http://demo.magestore.com/simicart/simipos3/index.php/";
    private String mSecretKey = "3f447fbaf2b16b42438bb747c8351c49faa4ab";
    private String mBaseCloudUrl = "https://api.jajahub.com/rest/";
    private String mSecretCloudKey = "c7b4e5216b79a8793ea56d9625fecffa614913e8";
    private String mColorSplashScreen = "#ffefef";
    private String mDemoEnable = "YES";
    private String mFontCustom = "fonts/ProximaNovaLight.ttf";
    private String mUseStore;
    private String isFullSplash = ""; //FULL_SPLASH
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

    public void setSecretKey(String secret_key) {
        mSecretKey = secret_key;
    }

    public String getSecretCloudKey() {
        return mSecretCloudKey;
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
