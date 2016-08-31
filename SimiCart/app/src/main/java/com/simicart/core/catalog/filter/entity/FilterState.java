package com.simicart.core.catalog.filter.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.filter.common.FilterConstant;

import java.io.Serializable;

public class FilterState extends SimiEntity {
    protected String mAttribute;
    protected String mTitle;
    protected String mLabel;
    protected String mValue;


    @Override
    public void parse() {
        mAttribute = getData(FilterConstant.ATTRIBUTE);
        mTitle = getData(FilterConstant.TITLE);
        mLabel = getData(FilterConstant.LABEL);
        mValue = getData(FilterConstant.VALUE);
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
