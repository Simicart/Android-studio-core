package com.simicart.plugins.storelocator.model;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.plugins.storelocator.entity.StoreObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ModelLocator extends SimiModel {

	protected String totalResult;

	@Override
	protected void setUrlAction() {
		mUrlAction = "storelocator/api/get_store_list";
	}

	@Override
	protected void parseData() {
		// TODO Auto-generated method stub
		Log.e("ModelLocator", mJSON.toString());
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			//list_store_object.clear();
			for (int i = 0; i < list.length(); i++) {
				JSONObject obj = list.getJSONObject(i);
				StoreObject storeObject = new StoreObject();
				storeObject.parse(obj);
				collection.addEntity(storeObject);
			}
			if(mJSON.has("message")) {
				JSONArray messArr = mJSON.getJSONArray("message");
				totalResult = messArr.getString(0);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTotalResult() {
		return totalResult;
	}
}
