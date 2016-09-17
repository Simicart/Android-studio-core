package com.simicart.plugins.storelocator.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.plugins.storelocator.entity.CountryObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GetSearchCountryModel extends SimiModel {

    protected ArrayList<CountryObject> listCountries;

    public ArrayList<CountryObject> getCountries() {
        return listCountries;
    }

    @Override
    protected void parseData() {
        try {
            JSONArray list = this.mJSON.getJSONArray("data");
            collection = new SimiCollection();
            listCountries = new ArrayList<>();
            for (int i = 0; i < list.length(); i++) {
                JSONObject obj = list.getJSONObject(i);
                CountryObject countryObject = new CountryObject();
                countryObject.parse(obj);
                listCountries.add(countryObject);
            }
            Collections.sort(listCountries, new Comparator<CountryObject>(){

                @Override
                public int compare(CountryObject lhs, CountryObject rhs) {
                    // TODO Auto-generated method stub
                    return (lhs.getCountry_name().substring(0, 1).compareToIgnoreCase(rhs.getCountry_name().substring(0, 1)));
                }
            });

            CountryObject countryObjectf = new CountryObject();
            countryObjectf.setCountry_code("");
            countryObjectf.setCountry_name(SimiTranslator.getInstance().translate("None"));
            listCountries.add(0, countryObjectf);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = "storelocator/api/get_country_list";
    }

    @Override
    protected void setEnableCache() {
        // TODO Auto-generated method stub
        enableCache = true;
    }

}
