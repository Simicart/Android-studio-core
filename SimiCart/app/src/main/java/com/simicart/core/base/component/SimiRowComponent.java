package com.simicart.core.base.component;

import android.view.View;

/**
 * Created by frank on 23/08/2016.
 */
public class SimiRowComponent extends SimiComponent {

    public enum TYPE_ROW {
        TEXT,
        NAVIGATION,
        ADAPTER,
        SELECT
    }

    protected String mKey;
    protected String mTitle;
    protected Object mValue;
    protected String mSuggestValue;
    protected boolean isRequired;
    protected TYPE_ROW mType;


    @Override
    public View createView() {
        initHeader();
        initBody();
        return rootView;
    }

    protected void initView() {

    }

    protected void initHeader() {

    }

    protected void initBody() {

    }

    public void updateView() {

    }

    public void setValue(Object value) {
        mValue = value;
    }

    public Object getValue() {
        return null;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public String getSuggestValue() {
        return mSuggestValue;
    }

    public void setSuggestValue(String mSuggestValue) {
        this.mSuggestValue = mSuggestValue;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public TYPE_ROW getType() {
        return mType;
    }

    public void setType(TYPE_ROW mType) {
        this.mType = mType;
    }
}
