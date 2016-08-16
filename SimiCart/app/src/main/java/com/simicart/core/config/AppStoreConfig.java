package com.simicart.core.config;

import android.util.Log;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;

import java.text.DecimalFormat;

/**
 * Created by Martial on 8/15/2016.
 */
public class AppStoreConfig extends SimiEntity {

    private String mCountryName;
    private String mCountryCode;
    private String mCurrencySymbol;
    private String mCurrencyCode;
    private String mViewProductDefault;
    private String mStoreID;
    private String mStoreName;
    private String mStoreCode;
    private String mLocaleIdentifier;
    private boolean mUseStore;
    private String mCurrencyPosition;
    private boolean isRTL;
    private boolean isShowZeroPrice;
    private boolean isShowLinkAllProduct;
    private boolean isReloadPaymentMethod;
    private String mSenderID = "";

    private String country_code = "country_code";
    private String country_name = "country_name";
    private String locale_identifier = "locale_identifier";
    private String store_id = "store_id";
    private String store_name = "store_name";
    private String store_code = "store_code";
    private String use_store = "use_store";
    private String is_rtl = "is_rtl";
    private String currency_symbol = "currency_symbol";
    private String currency_code = "currency_code";
    private String currency_position = "currency_position";
    private String is_show_zero_price = "is_show_zero_price";
    private String is_show_link_all_product = "is_show_link_all_product";
    private String is_reload_payment_method = "is_reload_payment_method";

    public static AppStoreConfig instance;

    public static AppStoreConfig newInstance() {
        if(instance == null) {
            instance = new AppStoreConfig();
        }
        return instance;
    }

    @Override
    public void parse() {
        // country code
        if ((hasKey(country_code))) {
            String code = getData(country_code);
            setCountryCode(code);
        }

        // country name
        if (hasKey(country_name)) {
            String name = getData(country_name);
            setCountryName(name);
        }


        // locale identifier
        if (hasKey(locale_identifier)) {
            String identifier = getData(locale_identifier);
            setLocaleIdentifier(identifier);
        }

        // store id
        if (hasKey(store_id)) {
            String id = getData(store_id);
            setStoreID(id);
        }
        
        // store name
        if(hasKey(store_name)) {
            String storeName = getData(store_name);
            setStoreName(storeName);
        }

        // store code
        if (hasKey(store_code)) {
            String code = getData(store_code);
            setStoreCode(code);
        }

        // is right to left
        if (hasKey(is_rtl)) {
            String rlt = getData(is_rtl);
            if (Utils.TRUE(rlt)) {
                setRTL(true);
            }
        }

        // currency symbol
        if (hasKey(currency_symbol)) {
            String symbol = getData(currency_symbol);
            setCurrencySymbol(symbol);
        }

        // currency code
        if (hasKey(currency_code)) {
            String code = getData(currency_code);
            setCurrencyCode(code);
        }

        //currency position
        if (hasKey(currency_position)) {
            String position = getData(currency_position);
            setCurrencyPosition(position);
        }
        
        if(hasKey(use_store)) {
            String useStore = getData(use_store);
            if(Utils.TRUE(useStore)) {
                setUseStore(true);
            }
        }

        if(hasKey(is_show_zero_price)) {
            String isShowZeroPrice = getData(is_show_zero_price);
            if(Utils.TRUE(isShowZeroPrice)) {
                setShowZeroPrice(true);
            }
        }

        if(hasKey(is_reload_payment_method)) {
            String isReloadPaymentMethod = getData(is_reload_payment_method);
            if(Utils.TRUE(isReloadPaymentMethod)) {
                setReloadPaymentMethod(true);
            }
        }

        if(hasKey(is_show_link_all_product)) {
            String isShowLinkAllProduct = getData(is_show_link_all_product);
            if(Utils.TRUE(isShowLinkAllProduct)) {
                setShowLinkAllProduct(true);
            }
        }

    }

    public String getPrice(String price) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        if (price == null || price.equals("null")) {
            price = "0";
        }
        float pricef = Float.parseFloat(price);
        price = df.format(pricef);
        if (mCurrencyPosition.equals("before")) {
            if ((null == mCurrencySymbol) || (mCurrencySymbol.equals("null"))
                    && null != mCurrencyCode && !mCurrencyCode.equals("null")) {
                return mCurrencyCode + price;
            } else {
                return mCurrencySymbol + price;
            }
        } else {
            if ((null == mCurrencySymbol) || (mCurrencySymbol.equals("null"))
                    && null != mCurrencyCode && !mCurrencyCode.equals("null")) {
                return price + " " + mCurrencyCode;
            } else {
                return price + " " + mCurrencySymbol;
            }
        }
    }

    public String getPrice(String price, String symbol) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        if (price == null || price.equals("null")) {
            price = "0";
        }
        float pricef = Float.parseFloat(price);
        price = df.format(pricef);
        if (mCurrencyPosition.equals("before")) {
            if ((null == symbol) || (symbol.equals("null"))
                    && null != mCurrencyCode && !mCurrencyCode.equals("null")) {
                return mCurrencyCode + price;
            } else {
                return symbol + price;
            }
        } else {
            if ((null == mCurrencySymbol) || (mCurrencySymbol.equals("null"))
                    && null != mCurrencyCode && !mCurrencyCode.equals("null")) {
                return price + " " + mCurrencyCode;
            } else {
                return price + " " + mCurrencySymbol;
            }
        }
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String mCountryCode) {
        this.mCountryCode = mCountryCode;
    }

    public String getCountryName() {
        return mCountryName;
    }

    public void setCountryName(String mCountryName) {
        this.mCountryName = mCountryName;
    }

    public String getLocaleIdentifier() {
        return mLocaleIdentifier;
    }

    public void setLocaleIdentifier(String mLocaleIdentifier) {
        this.mLocaleIdentifier = mLocaleIdentifier;
    }

    public boolean isRTL() {
        return isRTL;
    }

    public void setRTL(boolean RTL) {
        isRTL = RTL;
    }

    public String getCurrencySymbol() {
        return mCurrencySymbol;
    }

    public void setCurrencySymbol(String mCurrencySymbol) {
        this.mCurrencySymbol = mCurrencySymbol;
    }

    public String getCurrencyCode() {
        return mCurrencyCode;
    }

    public void setCurrencyCode(String mCurrencyCode) {
        this.mCurrencyCode = mCurrencyCode;
    }

    public String getCurrencyPosition() {
        return mCurrencyPosition;
    }

    public void setCurrencyPosition(String currencyPosition) {
        this.mCurrencyPosition = currencyPosition;
    }

    public String getStoreCode() {
        return mStoreCode;
    }

    public void setStoreCode(String storeCode) {
        this.mStoreCode = storeCode;
    }

    public String getStoreID() {
        return mStoreID;
    }

    public void setStoreID(String storeID) {
        this.mStoreID = storeID;
    }

    public String getStoreName() {
        return mStoreName;
    }

    public void setStoreName(String storeName) {
        this.mStoreName = storeName;
    }

    public boolean getUseStore() {
        return mUseStore;
    }

    public void setUseStore(boolean mUseStore) {
        this.mUseStore = mUseStore;
    }

    public boolean isShowZeroPrice() {
        return isShowZeroPrice;
    }

    public void setShowZeroPrice(boolean showZeroPrice) {
        isShowZeroPrice = showZeroPrice;
    }

    public boolean isShowLinkAllProduct() {
        return isShowLinkAllProduct;
    }

    public void setShowLinkAllProduct(boolean showLinkAllProduct) {
        isShowLinkAllProduct = showLinkAllProduct;
    }

    public boolean isReloadPaymentMethod() {
        return isReloadPaymentMethod;
    }

    public void setReloadPaymentMethod(boolean reloadPaymentethod) {
        isReloadPaymentMethod = reloadPaymentethod;
    }

    public String getViewProductDefault() {
        return mViewProductDefault;
    }

    public void setViewProductDefault(String viewProductDefault) {
        this.mViewProductDefault = viewProductDefault;
    }

    public String getSenderID() {
        return mSenderID;
    }

    public void setSenderID(String senderID) {
        this.mSenderID = senderID;
    }
}