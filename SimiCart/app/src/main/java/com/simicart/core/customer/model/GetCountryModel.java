package com.simicart.core.customer.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Constants;
import com.simicart.core.customer.entity.CountryEntity;

import org.json.JSONArray;
import org.json.JSONException;

public class GetCountryModel extends SimiModel {
    @Override
    protected void parseData() {
        try {
            JSONArray list = this.mJSON.getJSONArray("data");
            collection = new SimiCollection();
            for (int i = 0; i < list.length(); i++) {
                CountryEntity country_allowed = new CountryEntity();
                country_allowed.parse(list.getJSONObject(i));
                collection.addEntity(country_allowed);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = Constants.GET_ALLOWED_COUNTRIES;
    }

    @Override
    protected void setEnableCache() {
        this.enableCache = true;
    }
}
