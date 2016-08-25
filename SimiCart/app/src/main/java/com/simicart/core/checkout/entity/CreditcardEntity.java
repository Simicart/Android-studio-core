package com.simicart.core.checkout.entity;

import com.simicart.core.base.model.entity.SimiEntity;

/**
 * Created by frank on 7/2/16.
 */
public class CreditCardEntity extends SimiEntity {

    protected String mCode;
    protected String mName;
    protected String mType;
    protected String mCId;
    protected String mExpMonth;
    protected String mNumber;
    protected String mExpYear;

    private String cc_code = "cc_code";
    private String cc_name = "cc_name";
    private String cc_type = "cc_type";
    private String cc_cid = "cc_cid";
    private String cc_exp_month = "cc_exp_month";
    private String cc_number = "cc_number";
    private String cc_exp_year = "cc_exp_year";

    @Override
    public void parse() {

        mCode = getData(cc_code);

        mName = getData(cc_name);


        mType = getData(cc_type);

        mCId = getData(cc_cid);

        mExpMonth = getData(cc_exp_month);

        mNumber = getData(cc_number);

        mExpYear = getData(cc_exp_year);


    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String mCode) {
        this.mCode = mCode;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getCId() {
        return mCId;
    }

    public void setCId(String mCId) {
        this.mCId = mCId;
    }

    public String getExpMonth() {
        return mExpMonth;
    }

    public void setExpMonth(String mExpMonth) {
        this.mExpMonth = mExpMonth;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String mNumber) {
        this.mNumber = mNumber;
    }

    public String getExpYear() {
        return mExpYear;
    }

    public void setExpYear(String mExpYear) {
        this.mExpYear = mExpYear;
    }
}
