package com.simicart.core.customer.entity;

import com.simicart.core.base.model.entity.SimiEntity;

public class StateEntity extends SimiEntity {

    private String mID;
    private String mName;
    private String mCode;


    protected String STATE_ID = "state_id";
    protected String STATE_NAME = "state_name";
    protected String STATE_CODE = "state_code";


    @Override
    public void parse() {
        mID = getData(STATE_ID);

        mName = getData(STATE_NAME);

        mCode = getData(STATE_CODE);
    }

    public String getID() {
        return mID;
    }

    public void setID(String id) {
        this.mID = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String state_name) {
        this.mName = state_name;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String state_code) {
        this.mCode = state_code;
    }

}
