package com.simicart.core.customer.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class CountryAllowed extends SimiEntity {
    protected String mCode;
    protected String mName;
    protected ArrayList<StateOfCountry> stateList;

    protected String COUNTRY_NAME = "country_name";
    protected String COUNTRY_CODE = "country_code";
    protected String STATES = "states";

    @Override
    public void parse() {
        mCode = getData(COUNTRY_CODE);
        mName = getData(COUNTRY_NAME);
        try {
            JSONArray array = new JSONArray(getData(STATES));
            if (array.length() > 0) {
                stateList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    StateOfCountry state = new StateOfCountry();
                    state.setJSONObject(obj);
                    stateList.add(state);
                }
            }
        } catch (JSONException e) {
        }
    }


    public String getCode() {
        return mCode;
    }

    public void setCountry_code(String country_code) {
        this.mCode = country_code;
    }

    public String getName() {
        return mName;
    }

    public void setName(String country_name) {
        this.mName = country_name;
    }

    public ArrayList<StateOfCountry> getStateList() {
        return stateList;
    }

    public void setStateList(ArrayList<StateOfCountry> stateList) {
        this.stateList = stateList;
    }

}
