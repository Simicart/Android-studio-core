package com.simicart.core.checkout.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by frank on 6/29/16.
 */
public class ShippingMethodEntity extends SimiEntity {

    protected String mID;
    protected String mCode;
    protected String mTitle;
    protected String mFee;
    protected String mName;
    protected boolean isSelected;

    private String s_method_id = "s_method_id";
    private String s_method_code = "s_method_code";
    private String s_method_title = "s_method_title";
    private String s_method_fee = "s_method_fee";
    private String s_method_name = "s_method_name";
    private String s_method_selected = "s_method_selected";

    @Override
    public void parse() {

        if (hasKey(s_method_id)) {
            mID = getData(s_method_id);
        }

        if (hasKey(s_method_code)) {
            mCode = getData(s_method_code);
        }

        if (hasKey(s_method_title)) {
            mTitle = getData(s_method_title);
        }

        if (hasKey(s_method_fee)) {
            mFee = getData(s_method_fee);
        }

        if (hasKey(s_method_name)) {
            mName = getData(s_method_name);
        }

        if (hasKey(s_method_selected)) {
            String selected = getData(s_method_selected);
            if (Utils.TRUE(selected)) {
                setSelected(true);
            }
        }

    }


    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String mCode) {
        this.mCode = mCode;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getFee() {
        return mFee;
    }

    public void setFee(String mFee) {
        this.mFee = mFee;
    }

    public String getName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
