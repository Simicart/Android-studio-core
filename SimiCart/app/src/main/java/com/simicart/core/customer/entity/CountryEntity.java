package com.simicart.core.customer.entity;

import android.util.Log;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.entity.SimiEntity;

public class CountryEntity extends SimiEntity {
    protected String mCode;
    protected String mName;
    protected ArrayList<StateEntity> stateList;

    protected String COUNTRY_NAME = "country_name";
    protected String COUNTRY_CODE = "country_code";
    protected String STATES = "states";

    @Override
    public void parse() {
        mCode = getData(COUNTRY_CODE);
        mName = getData(COUNTRY_NAME);
        try {
            if (hasKey(STATES)) {
                JSONArray array = getJSONArrayWithKey(mJSON, STATES);
                if (null != array && array.length() > 0) {
                    stateList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        StateEntity state = new StateEntity();
                        state.parse(obj);
                        stateList.add(state);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("CountryEntity", " JSONException " + e.getMessage());
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

    public ArrayList<StateEntity> getStateList() {
        return stateList;
    }

    public void setStateList(ArrayList<StateEntity> stateList) {
        this.stateList = stateList;
    }

}
