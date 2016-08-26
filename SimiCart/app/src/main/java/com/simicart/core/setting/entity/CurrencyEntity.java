package com.simicart.core.setting.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;

public class CurrencyEntity extends SimiEntity {

    protected String value;
    protected String title;

    protected String VALUE = "value";
    protected String TITLE = "title";

    public String getValue() {
        if (!Utils.validateString(value)) {
            value = getData(VALUE);
        }
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        if (!Utils.validateString(title)) {
            title = getData(TITLE);
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
