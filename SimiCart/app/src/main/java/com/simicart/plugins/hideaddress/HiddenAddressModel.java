package com.simicart.plugins.hideaddress;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Constants;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.GenderConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Glenn on 9/6/2016.
 */
public class HiddenAddressModel extends SimiModel {

    @Override
    protected void parseData() {
        try {
            JSONArray list = this.mJSON.getJSONArray(Constants.DATA);
            collection = new SimiCollection();
            if (null != list && list.length() > 0) {
                JSONObject object = list.getJSONObject(0);
                paserConfigAddress(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void paserConfigAddress(JSONObject object) {
        try {

            String prefix = object.getString("prefix");
            if(prefix != null) {
                ConfigCustomerAddress.getInstance().setPrefix(prefix);
            }

            String suffix = object.getString("suffix");
            if(suffix != null) {
                ConfigCustomerAddress.getInstance().setSuffix(suffix);
            }

            String company = object.getString("company");
            if(company != null) {
                ConfigCustomerAddress.getInstance().setCompany(company);
            }

            String street = object.getString("street");
            if(street != null) {
                ConfigCustomerAddress.getInstance().setStreet(street);
            }

            String country = object.getString("country");
            if(country != null) {
                ConfigCustomerAddress.getInstance().setCountry(country);
            }

            String taxvat = object.getString("taxvat");
            if(taxvat != null) {
                ConfigCustomerAddress.getInstance().setTaxvat(taxvat);
            }

            String city = object.getString("city");
            if(city != null) {
                ConfigCustomerAddress.getInstance().setCity(city);
            }

            String state = object.getString("state");
            if(state != null) {
                ConfigCustomerAddress.getInstance().setState(state);
            }

            String zipcode = object.getString("zipcode");
            if(zipcode != null) {
                ConfigCustomerAddress.getInstance().setZipcode(zipcode);
            }

            String telephone = object.getString("telephone");
            if(telephone != null) {
                ConfigCustomerAddress.getInstance().setTelephone(telephone);
            }

            String fax = object.getString("fax");
            if(fax != null) {
                ConfigCustomerAddress.getInstance().setFax(fax);
            }

            String dob = object.getString("birthday");
            if(dob != null) {
                ConfigCustomerAddress.getInstance().setDob(dob);
            }

            String gender = object.getString("gender");
            if(gender != null) {
                ConfigCustomerAddress.getInstance().setGender(gender);
            }

            JSONArray genderConfigArr = object.getJSONArray("gender_value");
            if(genderConfigArr != null) {
                ArrayList<GenderConfig> genderConfigs = new ArrayList<>();
                for(int i=0;i<genderConfigArr.length();i++) {
                    JSONObject configObj = genderConfigArr.getJSONObject(i);
                    GenderConfig genderConfig = new GenderConfig();
                    genderConfig.parse(configObj);
                    genderConfigs.add(genderConfig);
                }
                ConfigCustomerAddress.getInstance().setGenderConfigs(genderConfigs);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setShowNotifi() {
        isShowNotify = false;
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = "hideaddress/api/get_address_show";
    }

}
