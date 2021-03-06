package com.simicart.core.catalog.product.entity;

import com.simicart.core.base.model.entity.SimiEntity;

public class Attributes extends SimiEntity {
    public static String TITLE = "title";
    protected String VALUE = "value";
    private String mTitle;
    private String mValue;

    @Override
    public void parse() {
        mTitle = getData(TITLE);

        mValue = getData(VALUE);

    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        this.mValue = value;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }


}
