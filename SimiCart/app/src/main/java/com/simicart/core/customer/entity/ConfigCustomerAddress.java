package com.simicart.core.customer.entity;

import java.util.ArrayList;

import org.json.JSONException;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;

public class ConfigCustomerAddress extends SimiEntity {
    public static String OPTION_REQUIRE = "req";
    public static String OPTION_OPTIONAL = "opt";
    public static String OPTION_HIDE = "";

    protected String prefix;
    protected String suffix;
    protected String dob;
    protected String vat_id;
    protected String taxvat;
    protected String gender;
    protected ArrayList<GenderConfig> genderConfigs = new ArrayList<>();
    protected String company;
    protected String street;
    protected String country;
    protected String state;
    protected String city;
    protected String zipcode;
    protected String telephone;
    protected String fax;
    protected String name;
    protected String email;

    protected static ConfigCustomerAddress instance;

    public static ConfigCustomerAddress getInstance() {
        if (null == instance) {
            instance = new ConfigCustomerAddress();
        }

        return instance;
    }

    protected String prefix_show = "prefix_show";
    protected String suffix_show = "suffix_show";
    protected String dob_show = "dob_show";
    protected String taxvat_show = "taxvat_show";
    protected String gender_show = "gender_show";
    protected String gender_value = "gender_value";

    public ConfigCustomerAddress() {
        setPrefix(OPTION_HIDE);
        setName(OPTION_REQUIRE);
        setSuffix(OPTION_HIDE);
        setEmail(OPTION_REQUIRE);
        setCompany(OPTION_HIDE);
        setVat_id(OPTION_HIDE);
        setStreet(OPTION_REQUIRE);
        setCity(OPTION_REQUIRE);
        setState(OPTION_OPTIONAL);
        setCountry(OPTION_REQUIRE);
        setZipcode(OPTION_REQUIRE);
        setTelephone(OPTION_REQUIRE);
        setFax(OPTION_HIDE);
        setDob(OPTION_HIDE);
        setGender(OPTION_HIDE);
        setTaxvat(OPTION_HIDE);
    }

    @Override
    public void parse() {

        prefix = getData(prefix_show);
        suffix = getData(suffix_show);
        dob = getData(dob_show);
        taxvat = getData(taxvat_show);
        gender = getData(gender_show);
        
    }

    public String getValueWithKey(String key) {
        switch (key) {
            case "prefix":
                return getPrefix();
            case "name":
                return getName();
            case "suffix":
                return getSuffix();
            case "email":
                return getEmail();
            case "company":
                return getCompany();
            case "street":
                return getStreet();
            case "taxvat":
                return getTaxvat();
            case "city":
                return getCity();
            case "state":
                return getState();
            case "country":
                return getCountry();
            case "zipcode":
                return getZipcode();
            case "phone":
                return getTelephone();
            case "fax":
                return getFax();
            case "datebirth":
                return getDob();
            case "gender":
                return getGender();
            default:
                return "";
        }
    }

    public void setVat_id(String vat_id) {
        this.vat_id = vat_id;
    }

    public String getVat_id() {
        return vat_id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getTaxvat() {
        return taxvat;
    }

    public void setTaxvat(String taxvat) {
        this.taxvat = taxvat;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<GenderConfig> getGenderConfigs() {
        return genderConfigs;
    }

    public void setGenderConfigs(ArrayList<GenderConfig> genderConfigs) {
        this.genderConfigs = genderConfigs;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isRequired(String value) {
        if (null == value) {
            return false;
        }

        value = value.toLowerCase();

        if (value.equals("req")) {
            return true;
        }
        return false;
    }

    public boolean isHidden(String value) {
        if (null == value) {
            return false;
        }

        if (value.equals("")) {
            return true;
        }

        return false;

    }

    public boolean isOptional(String value) {
        if (null == value) {
            return true;
        }

        value = value.toLowerCase();
        if (value.equals("opt")) {
            return true;
        }

        return false;

    }

    @Override
    public String getData(String key) {
        if (hasKey(key)) {
            try {
                return this.mJSON.getString(key);
            } catch (JSONException e) {
                return OPTION_OPTIONAL;
            }
        }
        return OPTION_OPTIONAL;
    }

}