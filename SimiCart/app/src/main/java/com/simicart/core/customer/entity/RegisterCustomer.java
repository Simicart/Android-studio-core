package com.simicart.core.customer.entity;

import com.simicart.core.base.model.entity.SimiEntity;

public class RegisterCustomer extends SimiEntity {
    protected String EMAIL = "email";
    protected String PREFIX = "prefix";
    protected String SUFFIX = "suffix";
    protected String DAY = "day";
    protected String MONTH = "month";
    protected String YEAR = "year";
    protected String GENDER = "gender";
    protected String NAME = "name";
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

    public String getPrefix() {
        if (mPrefix == null) {
            mPrefix = getData(PREFIX);
        }
        return mPrefix;
    }

    public void setPrefix(String mPrefix) {
        this.mPrefix = mPrefix;
    }

    public String getName() {
        if (mName == null) {
            mName = getData(NAME);
        }
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getSuffix() {
        if (mSuffix == null) {
            mSuffix = getData(SUFFIX);
        }
        return mSuffix;
    }

    public void setSuffix(String mSuffix) {
        this.mSuffix = mSuffix;
    }

    public String getEmail() {
        if (mEmail == null) {
            mEmail = getData(EMAIL);
        }
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getDay() {
        if (mDay == null) {
            mDay = getData(DAY);
        }
        return mDay;
    }

    public void setDay(String mDay) {
        this.mDay = mDay;
    }

    public String getMonth() {
        if (mMonth == null) {
            mMonth = getData(MONTH);
        }
        return mMonth;
    }

    public void setMonth(String mMonth) {
        this.mMonth = mMonth;
    }

    public String getYear() {
        if (mYear == null) {
            mYear = getData(YEAR);
        }
        return mYear;
    }

    public void setYear(String mYear) {
        this.mYear = mYear;
    }

    public String getGender() {
        if (mGender == null) {
            mGender = getData(GENDER);
        }
        return mGender;
    }

    public void setGender(String mGender) {
        this.mGender = mGender;
    }

    public String getTaxVat() {
        if (mTaxVat == null) {
            mTaxVat = getData(GENDER);
        }
        return mTaxVat;
    }

    public void setTaxVat(String mTaxVat) {
        this.mTaxVat = mTaxVat;
    }

    public String getPass() {
        return mPass;
    }

    public void setPass(String mPass) {
        this.mPass = mPass;
    }

    public String getConfirmPass() {
        return mConfirmPass;
    }

    public void setConfirmPass(String mConfirmPass) {
        this.mConfirmPass = mConfirmPass;
    }
}
