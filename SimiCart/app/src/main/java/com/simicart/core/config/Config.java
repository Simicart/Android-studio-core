package com.simicart.core.config;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.simicart.core.common.Utils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("DefaultLocale")
public class Config {

    private String mBaseUrl = "http://dev-vn.magestore.com/simicart/1800/index.php/";
    private String mSecretKey = "63d7615890f126fc6569198ae5607e5e9e184";
    private String mBaseCloudUrl = "http://dev-api.jajahub.com/rest";
    private String mSecretCloudKey = "9631ac14616756b5fdb7ba46750fdf18867325a7";

    private String mDemoEnable = "DEMO_ENABLE11";
    private String mColorSplashScreen = "#FFFFFF";
    private String mFontCustom = "fonts/ProximaNovaLight.ttf";

    private String mCookie = "";

    private int mGuestCheckout = 1;
    private int mEnableAgreements = 0;

    private int mTheme = 0; // 0 : default, 1 : matrix , 2 ztheme

    private static Config instance;

    public static Config getInstance() {
        if (null == instance) {
            instance = new Config();
        }

        return instance;
    }

    public String getDemoEnable() {
        return mDemoEnable;
    }

    public void setDemoEnable(String demo_enable) {
        mDemoEnable = demo_enable;
    }

    public int getTheme() {
        return mTheme;
    }

    public void setTheme(int mTheme) {
        this.mTheme = mTheme;
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

    public int getGuest_checkout() {
        return mGuestCheckout;
    }

    public void setGuest_checkout(int guest_checkout) {
        mGuestCheckout = guest_checkout;
    }

    public int getEnable_agreements() {
        return mEnableAgreements;
    }

    public void setEnable_agreements(int enable_agreements) {
        mEnableAgreements = enable_agreements;
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
