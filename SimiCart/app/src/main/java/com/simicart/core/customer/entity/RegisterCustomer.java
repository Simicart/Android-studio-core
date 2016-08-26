package com.simicart.core.customer.entity;

import com.simicart.core.base.model.entity.SimiEntity;

public class RegisterCustomer extends SimiEntity {
    private String mPrefix;
    private String mName;
    private String mSuffix;
    private String mEmail;
    private String mDay = "";
    private String mMonth = "";
    private String mYear = "";
    private String mGender;
    private String mTaxVat;
    private String mPass;
    private String mConfirmPass;

    protected String EMAIL = "email";
    protected String PREFIX = "prefix";
    protected String SUFFIX = "suffix";
    protected String DAY = "day";
    protected String MONTH = "month";
    protected String YEAR = "year";
    protected String GENDER = "gender";
    protected String NAME = "name";


    public void setPrefix(String mPrefix) {
        this.mPrefix = mPrefix;
    }

    public String getPrefix() {
        if (mPrefix == null) {
            mPrefix = getData(PREFIX);
        }
        return mPrefix;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getName() {
        if (mName == null) {
            mName = getData(NAME);
        }
        return mName;
    }

    public void setSuffix(String mSuffix) {
        this.mSuffix = mSuffix;
    }

    public String getSuffix() {
        if (mSuffix == null) {
            mSuffix = getData(SUFFIX);
        }
        return mSuffix;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getEmail() {
        if (mEmail == null) {
            mEmail = getData(EMAIL);
        }
        return mEmail;
    }

    public void setDay(String mDay) {
        this.mDay = mDay;
    }

    public String getDay() {
        if (mDay == null) {
            mDay = getData(DAY);
        }
        return mDay;
    }

    public void setMonth(String mMonth) {
        this.mMonth = mMonth;
    }

    public String getMonth() {
        if (mMonth == null) {
            mMonth = getData(MONTH);
        }
        return mMonth;
    }

    public void setYear(String mYear) {
        this.mYear = mYear;
    }

    public String getYear() {
        if (mYear == null) {
            mYear = getData(YEAR);
        }
        return mYear;
    }

    public void setGender(String mGender) {
        this.mGender = mGender;
    }

    public String getGender() {
        if (mGender == null) {
            mGender = getData(GENDER);
        }
        return mGender;
    }

    public void setTaxVat(String mTaxVat) {
        this.mTaxVat = mTaxVat;
    }

    public String getTaxVat() {
        if (mTaxVat == null) {
            mTaxVat = getData(GENDER);
        }
        return mTaxVat;
    }

    public void setPass(String mPass) {
        this.mPass = mPass;
    }

    public String getPass() {
        return mPass;
    }

    public void setConfirmPass(String mConfirmPass) {
        this.mConfirmPass = mConfirmPass;
    }

    public String getConfirmPass() {
        return mConfirmPass;
    }
}
