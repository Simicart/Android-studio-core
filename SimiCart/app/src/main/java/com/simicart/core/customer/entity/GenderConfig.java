package com.simicart.core.customer.entity;

import com.simicart.core.base.model.entity.SimiEntity;

public class GenderConfig extends SimiEntity {
    protected String mLabel;
    protected String mValue;

    protected String LABEL = "label";
    protected String VALUE = "value";


    @Override
    public void parse() {
        mLabel = getData(LABEL);

        mValue = getData(VALUE);

    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        this.mLabel = label;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        this.mValue = value;
    }


}
