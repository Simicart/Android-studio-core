package com.simicart.core.catalog.categorydetail.entity;

import com.simicart.core.base.model.entity.SimiEntity;

public class FilterState extends SimiEntity {
    public String ATTRIBUTE = "attribute";
    public String TITLE = "title";
    public String VALUE = "value";
    public String LABEL = "label";
    protected String mAttribute;
    protected String mTitle;
    protected String mLabel;
    protected String mValue;

    @Override
    public void parse() {
        mAttribute = getData(ATTRIBUTE);
        mTitle = getData(TITLE);
        mLabel = getData(LABEL);
        mValue = getData(VALUE);
    }

    public String getAttribute() {
        return mAttribute;
    }

    public void setAttribute(String mAttribute) {
        this.mAttribute = mAttribute;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String mLabel) {
        this.mLabel = mLabel;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String mValue) {
        this.mValue = mValue;
    }


}
