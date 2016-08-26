package com.simicart.core.customer.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Constants;
import com.simicart.core.customer.entity.AddressEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewAddressBookModel extends SimiModel {

	@Override
	protected void parseData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();

			if (null != list && list.length() > 0) {
				JSONObject json = list.getJSONObject(0);
				if (null != json) {
					AddressEntity address = new AddressEntity();
					address.setJSONObject(json);
					collection.addEntity(address);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void setUrlAction() {
		mUrlAction = Constants.SAVE_ADDRESS;
	}
}
