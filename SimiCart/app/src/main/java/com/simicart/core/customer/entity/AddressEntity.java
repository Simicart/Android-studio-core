package com.simicart.core.customer.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class AddressEntity extends SimiEntity {
    protected String mID;
    protected String mStateID;
    protected String mPrefix;
    protected String mName;
    protected String mSuffix;
    protected String mStreet;
    protected String mCity;
    protected String mStateName;
    protected String mStateCode;
    protected String mZip;
    protected String mCountryName;
    protected String mCountryCode;
    protected String mTaxvat;
    protected String mGender;
    protected String mDay;
    protected String mMonth;
    protected String mYear;
    protected String mPhone;
    protected String mEmail;
    protected String mFax;
    protected String mCompany;
    protected String mTaxVatCheckout;
    protected String mLatlng;

    protected String LATLNG = "latlng";
    protected String FAX = "fax";
    protected String COMPANY = "company";
    protected String ADDRESS_ID = "address_id";
    protected String STATE_ID = "state_id";
    protected String NAME = "name";
    protected String STREET = "street";
    protected String CITY = "city";
    protected String STATE_NAME = "state_name";
    protected String STATE_CODE = "state_code";
    protected String ZIP = "zip";
    protected String COUNTRY_NAME = "country_name";
    protected String COUNTRY_CODE = "country_code";
    protected String PHONE = "phone";
    protected String EMAIL = "email";
    protected String PREFIX = "prefix";
    protected String SUFFIX = "suffix";
    protected String TAXVAT = "taxvat";
    protected String GENDER = "gender";
    protected String DAY = "day";
    protected String MONTH = "month";
    protected String YEAR = "year";
    protected String TAXVAT_CHECKOUT = "vat_id";


    public AddressEntity() {
    }

    @Override
    public void parse() {

        mID = getData(ADDRESS_ID);
        mStateID = getData(STATE_ID);
        mPrefix = getData(PREFIX);
        mName = getData(NAME);
        mSuffix = getData(SUFFIX);
        mStreet = getData(STREET);
        mCity = getData(CITY);
        mStateName = getData(STATE_NAME);
        mStateCode = getData(STATE_CODE);
        mZip = getData(ZIP);
        mCountryName = getData(COUNTRY_NAME);
        mCountryCode = getData(COUNTRY_CODE);
        mTaxvat = getData(TAXVAT);
        mGender = getData(GENDER);
        mDay = getData(DAY);
        mMonth = getData(MONTH);
        mYear = getData(YEAR);
        mPhone = getData(PHONE);
        mEmail = getData(EMAIL);
        mFax = getData(FAX);
        mCompany = getData(COMPANY);
        mTaxVatCheckout = getData(TAXVAT_CHECKOUT);
        mLatlng = getData(LATLNG);
    }

    public JSONObject toParamForPlaceOrder()  {
        try {
            JSONObject json = new JSONObject();
            String addressID = mID;
            if (!Utils.validateString(mID) || mID.equals("-1")) {
                addressID = "0";
            }
            json.put("address_id", addressID);

            // name
            if (Utils.validateString(mName)) {
                json.put("name", mName);
            }

            // street
            if (Utils.validateString(mStreet)) {
                json.put("street", mStreet);
            }
            // city
            if (Utils.validateString(mCity)) {
                json.put("city", mCity);
            }
            // state name
            if (Utils.validateString(mStateName)) {
                json.put("state_name", mStateName);
                json.put("state_id", mStateID);
                json.put("state_code", mStateCode);
            }
            // country name
            if (Utils.validateString(mCountryName)) {
                json.put("country_name", mCountryName);
                json.put("country_code", mCountryCode);
            }
            // ZIP code
            String zipcode = getZipCode();
            if (Utils.validateString(mZip)) {
                json.put("zip", mZip);
                json.put("zip_code", mZip);
            }
            // phone
            String phone = getPhone();
            if (Utils.validateString(mPhone)) {
                json.put("phone", mPhone);
            }
            // email
            if (Utils.validateString(mEmail)) {
                json.put("email", mEmail);
            }
            // prefix
            if (Utils.validateString(mPrefix)) {
                json.put("prefix", mPrefix);
            }
            // suffix
            if (Utils.validateString(mSuffix)) {
                json.put("suffix", mSuffix);
            }
            // tax vat
            if (Utils.validateString(mTaxvat)) {
                json.put("taxvat", mTaxvat);
            }
            // tax vat check out
            if (Utils.validateString(mTaxVatCheckout)) {
                json.put("vat_id", mTaxVatCheckout);
            }
            // gender
            if (Utils.validateString(mGender)) {
                json.put("gender", Utils
                        .getValueGender(mGender));
            }
            if (Utils.validateString(mDay)) {
                String day = "";
                if (getDay().length() == 1) {
                    day = "0" + getDay();
                } else {
                    day = getDay();
                }
                json.put("day", day);
                String month = "";
                if (getMonth().length() == 1) {
                    month = "0" + getMonth();
                } else {
                    month = getMonth();
                }
                json.put("month", month);
                json.put("year", mYear);
                String dob = "" + month + "/" + day
                        + "/" + getYear() + "";
                json.put("dob", dob);
            }
            // Fax
            if (Utils.validateString(mFax)) {
                json.put("fax", mFax);
            }
            // company
            if (Utils.validateString(mCompany)) {
                json.put("company", mCompany);
            }
            return json;
        } catch (JSONException e) {
            return null;
        }

    }


    public String getLatlng() {
        return mLatlng;
    }

    public void setLatlng(String latlng) {
        this.mLatlng = latlng;
    }

    public String getFax() {
        return mFax;
    }

    public void setFax(String fax) {
        this.mFax = fax;
    }

    public String getCompany() {
        return mCompany;
    }

    public void setCompany(String company) {
        this.mCompany = company;
    }

    public String getID() {
        return mID;
    }

    public void setID(String addressId) {
        this.mID = addressId;
    }

    public String getStateId() {
        return mStateID;
    }

    public void setStateId(String stateId) {
        this.mStateID = stateId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getStreet() {
        return mStreet;
    }

    public void setStreet(String street) {
        this.mStreet = street;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity = city;
    }

    public String getStateName() {
        return mStateName;
    }

    public void setStateName(String stateName) {
        this.mStateName = stateName;
    }

    public String getStateCode() {

        return mStateCode;
    }

    public void setStateCode(String stateCode) {
        this.mStateCode = stateCode;
    }

    public String getZipCode() {
        return mZip;
    }

    public void setZipCode(String zipCode) {
        this.mZip = zipCode;
    }

    public String getCountryName() {
        return mCountryName;
    }

    public void setCountryName(String countryName) {
        this.mCountryName = countryName;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        this.mCountryCode = countryCode;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getPrefix() {
        return mPrefix;
    }

    public void setPrefix(String mPrefix) {
        this.mPrefix = mPrefix;
    }

    public String getSuffix() {
        return mSuffix;
    }

    public void setSuffix(String suffix) {
        this.mSuffix = suffix;
    }

    public String getTaxvat() {
        return mTaxvat;
    }

    public void setTaxvat(String taxvat) {
        this.mTaxvat = taxvat;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        this.mGender = gender;
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        this.mDay = day;
    }

    public String getMonth() {
        return mMonth;
    }

    public void setMonth(String month) {
        this.mMonth = month;
    }

    public String getYear() {
        return mYear;
    }

    public void setYear(String year) {
        this.mYear = year;
    }

    public void setTaxvatCheckout(String tax) {
        this.mTaxVatCheckout = tax;
    }

    public String getTaxvatCheckout() {
        return this.mTaxVatCheckout;
    }


}
