package com.simicart.core.catalog.categorydetail.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;

public class ValueFilterEntity extends SimiEntity {

    public String VALUE = "value";
    public String LABEL = "label";
    protected String mValue;
    protected boolean isSelected;
    protected String mLabel;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getmValue() {

        if (!Utils.validateString(mValue)) {
            mValue = getData(VALUE);
        }

        return mValue;
    }

    public void setmValue(String mValue) {
        this.mValue = mValue;
    }

    public String getLabel() {

        if (!Utils.validateString(mLabel)) {
            mLabel = getData(LABEL);
        }

        return mLabel;
    }

    public void setLabel(String mTitle) {
        this.mLabel = mTitle;
    }

}
