package com.simicart.core.catalog.product.entity;

import java.io.Serializable;

import android.os.Parcel;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class Attributes extends SimiEntity {
    private String mTitle;
    private String mValue;

    public static String TITLE = "title";
    protected String VALUE = "value";


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
