package com.simicart.plugins.locator.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.plugins.locator.entity.CountryObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
			for(int i = 0;i<list.length();i++) {
				JSONObject obj = list.getJSONObject(i);
				CountryObject countryObject = new CountryObject();
				countryObject.parse(obj);
				listCountries.add(countryObject);
			}
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
